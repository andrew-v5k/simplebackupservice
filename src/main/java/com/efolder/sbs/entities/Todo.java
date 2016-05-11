package com.efolder.sbs.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Todo implements Serializable {
	private static final long serialVersionUID = -8398130264579540773L;
	private String id;
	private String subject;
	private String dueDate;
	private String done;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDone() {
		return done;
	}

	public void setDone(String done) {
		this.done = done;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", subject=" + subject + ", dueDate=" + dueDate + ", done=" + done + "]";
	}
}