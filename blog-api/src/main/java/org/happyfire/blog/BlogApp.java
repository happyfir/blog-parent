package org.happyfire.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogApp {
    public static void main(String[] args) {
        SpringApplication.run(BlogApp.class,args);
    }
}
