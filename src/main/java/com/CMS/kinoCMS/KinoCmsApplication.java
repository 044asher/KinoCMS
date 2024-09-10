package com.CMS.kinoCMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KinoCmsApplication extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(KinoCmsApplication.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(KinoCmsApplication.class, args);
	}
}
