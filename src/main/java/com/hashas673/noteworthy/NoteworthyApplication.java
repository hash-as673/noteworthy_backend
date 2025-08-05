package com.hashas673.noteworthy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.slf4j.bridge.SLF4JBridgeHandler;


@SpringBootApplication
public class NoteworthyApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(NoteworthyApplication.class);
	}

	public static void main(String[] args) {

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		SpringApplication.run(NoteworthyApplication.class, args);
	}
}
