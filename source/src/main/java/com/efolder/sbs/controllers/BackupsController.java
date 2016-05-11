package com.efolder.sbs.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efolder.sbs.core.SbsApi;
import com.efolder.sbs.entities.BackupDto;

@RestController
public class BackupsController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SbsApi api;

	@RequestMapping(value = "/backups", method = POST)
	public ResponseEntity<BackupDto> startNewBackup() {
		try {
			BackupDto backup = api.startNewBackup();

			return new ResponseEntity<BackupDto>(backup, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("start new backup failed", e);
			return new ResponseEntity<BackupDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/backups", method = GET)
	public ResponseEntity<List<BackupDto>> readBackups() {
		try {
			List<BackupDto> backups = api.listBackups();
			return new ResponseEntity<List<BackupDto>>(backups, HttpStatus.OK);
		} catch (Exception e) {
			logger.info("can't retrieve backups list", e);
			return new ResponseEntity<List<BackupDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}