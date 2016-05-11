package com.efolder.sbs.core.export;

import java.io.IOException;

/**
 * Provides objects which can handle read/write operations for backup(s)
 */
public interface BackupsStorage<I, O> {
	/**
	 * get input by backup id
	 * 
	 * @param backupId
	 * @return
	 * @throws IOException
	 */
	I inputStream(String backupId) throws IOException;

	/**
	 * get output by backup id
	 * 
	 * @param backupId
	 * @return
	 * @throws IOException
	 */
	O outputStream(String backupId) throws IOException;
}