package com.efolder.sbs.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.efolder.sbs.core.export.BackupDataProcessor;
import com.efolder.sbs.dal.BackupDal;
import com.efolder.sbs.entities.BackupDto;
import com.efolder.sbs.entities.BackupStatus;
import com.efolder.sbs.entities.BackupTask;
import com.efolder.sbs.entities.Todo;
import com.efolder.sbs.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class SbsManagerTest {
	private static final String TODO_SERVER_URL = "http://127.0.0.1:34123";
	@Mock
	private BackupDataProcessor<InputStream> processor;
	@Mock
	private BackupDal backupDal;
	@InjectMocks
	@Spy
	private SbsManager sbs;

	private BackupTask dummyTask;
	private HttpEntity responseBody;

	private ObjectMapper mapper;
	private ExecutorService testExecutor;

	@Before
	public void setUp() throws JsonProcessingException, UnsupportedEncodingException {
		testExecutor = Executors.newCachedThreadPool();
		ReflectionTestUtils.setField(sbs, "backupsExecutor", testExecutor);
		ReflectionTestUtils.setField(sbs, "todoServerUrl", TODO_SERVER_URL);

		mapper = new ObjectMapper();

		dummyTask = new BackupTask();
		dummyTask.setId("1");
		dummyTask.setStatus(BackupStatus.IN_PROGRESS);

		responseBody = buildResponse();

		when(backupDal.newTask()).thenReturn(dummyTask);
	}

	@After
	public void after() {
		testExecutor.shutdownNow();
	}

	@Test
	public void shouldStartNewBackup() throws IOException, InterruptedException {
		CloseableHttpResponse response = mock(CloseableHttpResponse.class);
		when(response.getEntity()).thenReturn(responseBody);

		CloseableHttpClient client = mock(CloseableHttpClient.class);
		when(client.execute(any(HttpUriRequest.class))).thenReturn(response);

		when(sbs.getClient()).thenReturn(client);

		BackupDto newBackup = sbs.startNewBackup();

		testExecutor.awaitTermination(1L, TimeUnit.SECONDS);

		verify(backupDal).newTask();
		verify(processor).process(any(BackupTask.class), any(InputStream.class));
		verify(backupDal).updateStatus(dummyTask.getId(), BackupStatus.OK);

		assertEquals(newBackup, dummyTask.view());
	}

	@Test
	public void shouldRestartBackupIfRequestFail() throws ClientProtocolException, IOException, InterruptedException {
		CloseableHttpResponse response = mock(CloseableHttpResponse.class);
		when(response.getEntity()).thenReturn(responseBody);

		CloseableHttpClient client = mock(CloseableHttpClient.class);
		when(client.execute(any(HttpUriRequest.class))).thenThrow(new RuntimeException("test exception"));

		when(sbs.getClient()).thenReturn(client);

		BackupDto newBackup = sbs.startNewBackup();

		testExecutor.awaitTermination(1L, TimeUnit.SECONDS);

		verify(backupDal, atLeast(2)).newTask();
		verify(processor, never()).process(any(BackupTask.class), any(InputStream.class));
		verify(backupDal, atLeast(2)).updateStatus(dummyTask.getId(), BackupStatus.FAILED);

		assertEquals(newBackup, dummyTask.view());
	}

	@Test
	public void shouldRestartBackupIfParseFail() throws ClientProtocolException, IOException, InterruptedException {
		CloseableHttpResponse response = mock(CloseableHttpResponse.class);
		when(response.getEntity()).thenReturn(responseBody);

		CloseableHttpClient client = mock(CloseableHttpClient.class);
		when(client.execute(any(HttpUriRequest.class))).thenReturn(response);

		when(sbs.getClient()).thenReturn(client);
		doThrow(RuntimeException.class).when(processor).process(eq(dummyTask), any(InputStream.class));

		BackupDto newBackup = sbs.startNewBackup();

		testExecutor.awaitTermination(1L, TimeUnit.SECONDS);

		verify(backupDal, atLeast(2)).newTask();
		verify(processor, atLeast(2)).process(any(BackupTask.class), any(InputStream.class));
		verify(backupDal, atLeast(2)).updateStatus(dummyTask.getId(), BackupStatus.FAILED);

		assertEquals(newBackup, dummyTask.view());
	}

	private HttpEntity buildResponse() throws JsonProcessingException, UnsupportedEncodingException {
		User[] users = new User[] { buildUser("1") };

		return new StringEntity(mapper.writeValueAsString(users));
	}

	private User buildUser(String id) {
		User user = new User(id, "username1", "email1@email.com");
		user.setTodos(Arrays.asList(buildTodo(id)));
		return user;
	}

	private Todo buildTodo(String id) {
		Todo todo = new Todo();
		todo.setId(id);
		todo.setSubject("subj");
		todo.setDone("true");
		return todo;
	}
}