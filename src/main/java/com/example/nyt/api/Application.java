package com.example.nyt.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(),
				"/nyt/*");
		servlet.setName("CamelServlet");
		return servlet;
	}

	@Bean
	ObjectWriter getObjectWriter(){
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writer().withDefaultPrettyPrinter();
	}
}
