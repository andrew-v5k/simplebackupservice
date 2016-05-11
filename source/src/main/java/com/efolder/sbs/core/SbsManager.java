package com.efolder.sbs.core;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.efolder.sbs.core.export.BackupDataProcessor;
import com.efolder.sbs.dal.BackupDal;
import com.efolder.sbs.entities.BackupDto;
import com.efolder.sbs.entities.BackupStatus;
import com.efolder.sbs.entities.BackupTask;

@Component
public class SbsManager implements SbsApi {
	private static final String REQUEST_USERS = "/users";
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${todo.server.url}")
	private String todoServerUrl;
	@Autowired
	private Executor backupsExecutor;
	@Autowired
	private BackupDal backupDal;
	@Autowired
	private BackupDataProcessor<InputStream> processor;

	/* 
	 * Starts task in new thread:
	 * - get all users from TodoServer
	 * - process retrieved data and write into storage
	 * @see com.efolder.sbs.core.SbsApi#startNewBackup()
	 */
	@Override
	public BackupDto startNewBackup() {
		BackupTask task = backupDal.newTask();

		CompletableFuture.supplyAsync(() -> {
			HttpGet request = new HttpGet(url(REQUEST_USERS));
			try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request)) {
				processor.process(task, response.getEntity().getContent());
			} catch (Exception e) {
				logger.warn("Can't parse response for backup: {}", task.getId(), e);
				return false;
			}
			return true;
		}, backupsExecutor).thenAccept(s -> {
			logger.info("Backup finished. status: {}", s);
			backupDal.updateStatus(task.getId(), s ? BackupStatus.OK : BackupStatus.FAILED);

			CompletableFuture.runAsync(this::startNewBackup);
		});

		return task.view();
	}

	@Override
	public List<BackupDto> listBackups() {
		return backupDal.list();
	}

	private String url(String request) {
		return todoServerUrl + request;
	}
}