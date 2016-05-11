package com.backupservice;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.efolder.sbs.SmallBackupServiceApplication;
import com.efolder.sbs.SpringBeans;
import com.efolder.sbs.controllers.BackupsController;
import com.efolder.sbs.core.SbsApi;
import com.efolder.sbs.core.SbsManager;
import com.efolder.sbs.entities.BackupDto;
import com.efolder.sbs.entities.Todo;
import com.efolder.sbs.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SmallBackupServiceApplication.class, SpringBeans.class})
public class SmallBackupServiceApplicationTest {
	@Autowired
	private SbsManager sbs;
	private ObjectMapper mapper;
	private MockRestServiceServer todoServer;

	@Before
	public void setUp() {
		todoServer = MockRestServiceServer.createServer(new RestTemplate());
		mapper = new ObjectMapper();
	}

	@Test
	public void shouldCreateBackup() throws JsonProcessingException {
		User[] users = buildUsers();
		String todoServerResponse = mapper.writeValueAsString(users);

		todoServer.expect(requestTo("http://127.0.0.1:9000/users")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(todoServerResponse, MediaType.APPLICATION_JSON));

		BackupDto newBackup = sbs.startNewBackup();
		assertThat(newBackup, notNullValue());

		List<BackupDto> backups = sbs.listBackups();
		assertThat(backups.size(), equalTo(1));
	}

	private User[] buildUsers() {
		User user = buildUser("1");
		return new User[] { user };
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