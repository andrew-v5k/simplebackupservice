package com.efolder.sbs.entities;

import java.util.Date;

public class BackupDto {
	private String id;
	private Date date;
	private BackupStatus status;

	public BackupDto() {
	}

	public BackupDto(String id, Date date, BackupStatus status) {
		this.id = id;
		this.date = date;
		this.status = status;
	}

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

	@Override
	public String toString() {
		return "BackupDto [id=" + id + ", date=" + date + ", status=" + status + "]";
	}
}