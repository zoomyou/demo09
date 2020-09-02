package com.demo09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.websocket.server.ServerEndpoint;

@SpringBootApplication
public class Demo09Application implements ServletContextInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Demo09Application.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","1024000");
	}
}
