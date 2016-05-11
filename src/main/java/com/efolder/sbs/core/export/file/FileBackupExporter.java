package com.efolder.sbs.core.export.file;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efolder.sbs.core.ErrorCode;
import com.efolder.sbs.core.SbsException;
import com.efolder.sbs.core.export.BackupExporter;
import com.efolder.sbs.core.export.BackupsStorage;
import com.efolder.sbs.dal.BackupDal;
import com.efolder.sbs.entities.BackupDto;

@Component
public class FileBackupExporter implements BackupExporter<OutputStream> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BackupDal backupDal;
	@Autowired
	private BackupsStorage<InputStream, OutputStream> storage;

	@Override
	public void export(String backupId, OutputStream output) {
		BackupDto backup = backupDal.find(backupId);
		if (backup == null) {
			throw new RuntimeException(String.format("Export failed. backupId=%s is absent", backupId));
		}

		try (InputStream csvData = storage.inputStream(backupId)) {
			int size = IOUtils.copy(csvData, output);

			logger.debug("Wrote {} bytes for backup {}", size, backupId);
			output.flush();
		} catch (Exception e) {
			throw new SbsException(ErrorCode.EXPORT_FAILED, "An error occured during export");
		}
	}
}