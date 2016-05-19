package com.efolder.sbs.dal;

import java.util.List;

import com.efolder.sbs.entities.BackupDto;
import com.efolder.sbs.entities.BackupStatus;
import com.efolder.sbs.entities.BackupTask;

public interface BackupDal {
	BackupTask newTask();

	List<BackupDto> list();

	void updateStatus(String id, BackupStatus status);

	BackupDto find(String id);
}