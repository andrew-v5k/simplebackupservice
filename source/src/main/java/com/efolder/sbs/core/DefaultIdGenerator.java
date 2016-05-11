package com.efolder.sbs.core;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class DefaultIdGenerator implements IdGenerator {
	public String generateNextId() {
		return UUID.randomUUID().toString();
	}
}