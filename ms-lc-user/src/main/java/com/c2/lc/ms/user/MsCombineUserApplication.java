package com.c2.lc.ms.user;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class },
        scanBasePackages = {"com.c2.lc.lib", "com.c2.lc.ms.user"})
@EnableJpaRepositories("com.c2.lc.ms.user.repos")
@EntityScan("com.c2.lc.ms.user.entities")
public class MsCombineUserApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MsCombineUserApplication.class)
                .properties("spring.config.name:application,db,log", "spring.config.location:classpath:/")
                .build()
                .run(args);
    }
}
