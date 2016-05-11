package com.efolder.sbs.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
	private static final long serialVersionUID = 4201894243188720250L;
	private String id;
	private String username;
	private String email;
	private List<Todo> todos;

	public User() {
		todos = new ArrayList<>();
	}

	public User(String id, String username, String email) {
		this();
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", todos=" + todos + "]";
	}
}