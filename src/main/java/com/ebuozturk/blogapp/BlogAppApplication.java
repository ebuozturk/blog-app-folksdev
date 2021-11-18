package com.ebuozturk.blogapp;

import com.ebuozturk.blogapp.entity.Comment;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.repository.CommentRepository;
import com.ebuozturk.blogapp.repository.EntryRepository;
import com.ebuozturk.blogapp.repository.UserRepository;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(BlogAppApplication.class, args);

    }


    @Override
    public void run(String... args) throws Exception {

    }

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI();
    }
}
