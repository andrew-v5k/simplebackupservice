package com.efolder.sbs.core.export;

import java.io.IOException;

import com.efolder.sbs.entities.BackupTask;

/**
 * parse backup data 
 */
public interface BackupDataProcessor<T> {
	/**
	 * @param task
	 * @param data
	 * @throws IOException
	 */
	public void process(BackupTask task, T data) throws IOException;
}