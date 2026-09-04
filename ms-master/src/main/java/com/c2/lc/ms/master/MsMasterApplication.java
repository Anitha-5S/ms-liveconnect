package com.c2.lc.ms.master;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class },
		scanBasePackages = {"com.c2.lc.lib", "com.c2.lc.ms.master"})
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.c2.lc.ms.master.repos.elastic")
public class MsMasterApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MsMasterApplication.class)
				.properties("spring.config.name:application,db,log,spy", "spring.config.location:classpath:/")
				.build()
				.run(args);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		return new MultipartConfigElement("");
	}

	@Bean
	public MultipartResolver multipartResolver() {
		org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(1000000);
		return multipartResolver;
	}
}
