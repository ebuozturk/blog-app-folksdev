package com.ebuozturk.blogapp;

import com.ebuozturk.blogapp.entity.Comment;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.repository.CommentRepository;
import com.ebuozturk.blogapp.repository.EntryRepository;
import com.ebuozturk.blogapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EntryRepository entryRepository;

    public BlogAppApplication(UserRepository userRepository, CommentRepository commentRepository, EntryRepository entryRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.entryRepository = entryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogAppApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

//        User user = new User("ebuozturk");
//        user = userRepository.save(user);
//
//        Entry entry = new Entry(user,"content");
//        Comment comment = new Comment("comment",entry,user);
//        entry.getComments().add(comment);
//        entryRepository.save(entry);

    }
}
