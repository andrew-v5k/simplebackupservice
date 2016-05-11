package com.efolder.sbs.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.efolder.sbs.core.export.BackupExporter;

@RestController
public class ExportController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BackupExporter<OutputStream> exporter;

	@RequestMapping(value = "/exports/{backupId}", method = GET)
	public StreamingResponseBody export(@PathVariable String backupId) {
		return (output) -> {
			logger.info("start streaming export response. backup: {}", backupId);

			exporter.export(backupId, output);

			logger.info("streaming export response finished. backup: {}", backupId);
		};
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public void handleException() {
	}
}