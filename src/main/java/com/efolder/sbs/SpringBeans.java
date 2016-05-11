package com.efolder.sbs;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SpringBeans extends WebMvcConfigurerAdapter {
	@Bean
	public RestOperations restOperations() {
		return new RestTemplate();
	}

	@Bean
	public ObjectCodec objectCodec() {
		return new ObjectMapper();
	}

	@Bean
	public JsonFactory jsonFactory() {
		return new JsonFactory();
	}

	@Bean(name = "backupsExecutor")
	public Executor backupsExecutor() {
		return Executors.newCachedThreadPool();
	}
}