package com.efolder.sbs.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "backup_tasks")
public class BackupTask {
	@Id
	private String id;
	private Date date;
	private BackupStatus status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BackupStatus getStatus() {
		return status;
	}

	public void setStatus(BackupStatus status) {
		this.status = status;
	}

	public BackupDto view() {
		return new BackupDto(id, date, status);
	}
}