package com.efolder.sbs.core.export.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.efolder.sbs.core.export.BackupsStorage;

@Component
public class FileBackupStorage implements BackupsStorage<InputStream, OutputStream> {
	@Value("${backups.dir}")
	private String backupDir;

	@Override
	public InputStream inputStream(String backupId) throws IOException {
		return Files.newInputStream(getFile(backupId), StandardOpenOption.READ);
	}

	@Override
	public OutputStream outputStream(String backupId) throws IOException {
		return Files.newOutputStream(getFile(backupId), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
	}

	private Path getFile(String backupId) {
		return Paths.get(getFilename(backupId));
	}

	private String getFilename(String backupId) {
		return backupDir + backupId;
	}
}