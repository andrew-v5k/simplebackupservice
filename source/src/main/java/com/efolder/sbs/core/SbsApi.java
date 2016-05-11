package com.efolder.sbs.core;

import java.util.List;

import com.efolder.sbs.entities.BackupDto;

public interface SbsApi {
	/**
	 * Initiate new backup task
	 * 
	 * @return started backup task data
	 */
	BackupDto startNewBackup();

	/**
	 * retrieve list of all backup tasks
	 * 
	 * @return backups
	 */
	List<BackupDto> listBackups();
}