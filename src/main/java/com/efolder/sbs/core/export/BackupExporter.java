package com.efolder.sbs.core.export;

/**
 * Retrieves data for export and write into provided destination
 */
public interface BackupExporter<T> {
	/**
	 * find backup data by id and write backup items into output
	 * @param backupId
	 * @param output destination for exported data
	 */
	void export(String backupId, T output);
}