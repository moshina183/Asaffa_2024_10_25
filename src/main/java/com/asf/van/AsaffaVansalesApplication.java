package com.asf.van;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
// import org.springyframework.boot.web.servlet.support.SpringBootServletInitializer; // For springboot 2.0.x
import org.springframework.boot.web.support.SpringBootServletInitializer; // for springboot 1.5.x


@SpringBootApplication
public class AsaffaVansalesApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AsaffaVansalesApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AsaffaVansalesApplication.class, args);
    }
}
