package com.efolder.sbs.dal;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efolder.sbs.core.ErrorCode;
import com.efolder.sbs.core.IdGenerator;
import com.efolder.sbs.core.SbsException;
import com.efolder.sbs.entities.BackupDto;
import com.efolder.sbs.entities.BackupStatus;
import com.efolder.sbs.entities.BackupTask;

@Component
public class DefaultBackupDal implements BackupDal {
	@Autowired
	private IdGenerator ids;
	@Autowired
	private BackupRepository repository;

	public BackupTask newTask() {
		try {
			BackupTask task = new BackupTask();
			task.setId(ids.generateNextId());
			task.setDate(new Date());
			task.setStatus(BackupStatus.IN_PROGRESS);
			
			return repository.save(task);
		} catch (Exception e) {
			throw new SbsException(ErrorCode.CANT_PERSIST_TASK, "Backup task was not saved");
		}
	}

	public List<BackupDto> list() {
		try {
			return StreamSupport.stream(repository.findAll().spliterator(), false)
					.map(BackupTask::view)
					.collect(Collectors.toList());

		} catch (Exception e) {
			throw new SbsException(ErrorCode.CANT_RETRIEVE_TASK, "Getting task list failed");
		}
	}

	public void updateStatus(String id, BackupStatus status) {
		try {
			BackupTask task = repository.findOne(id);
			task.setStatus(status);
			repository.save(task);
		} catch (Exception e) {
			throw new SbsException(ErrorCode.CANT_UPDATE_STATUS, String.format("Status was not updated to %s for task=%s", status.name(), id));
		}
	}

	public BackupDto find(String id) {
		try {
			BackupTask task = repository.findOne(id);
			return task == null ? null : task.view();
		} catch (Exception e) {
			throw new SbsException(ErrorCode.CANT_RETRIEVE_TASK, String.format("Getting task=%s failed", id));
		}
	}
}