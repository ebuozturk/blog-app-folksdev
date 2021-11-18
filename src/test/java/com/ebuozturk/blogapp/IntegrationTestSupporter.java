package com.ebuozturk.blogapp;


import com.ebuozturk.blogapp.converter.CommentConverter;
import com.ebuozturk.blogapp.converter.EntryConverter;
import com.ebuozturk.blogapp.converter.UserConverter;
import com.ebuozturk.blogapp.dto.comment.CommentDto;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.entity.Comment;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.repository.CommentRepository;
import com.ebuozturk.blogapp.repository.EntryRepository;
import com.ebuozturk.blogapp.repository.UserRepository;
import com.ebuozturk.blogapp.service.CommentService;
import com.ebuozturk.blogapp.service.EntryService;
import com.ebuozturk.blogapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(value = "classpath:application.properties")
@DirtiesContext
@AutoConfigureMockMvc
public class IntegrationTestSupporter {

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserConverter userConverter;

    @Autowired
    public EntryRepository entryRepository;

    @Autowired
    public EntryService entryService;

    @Autowired
    public EntryConverter entryConverter;

    @Autowired
    public CommentRepository commentRepository;

    @Autowired
    public CommentService commentService;

    @Autowired
    public CommentConverter commentConverter;


    public final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
        entryRepository.deleteAll();
        commentRepository.deleteAll();
    }


// USER

    public User generateUser(String id){
        return new User(
                id,
                "username"+id,
                "first name"+id,
                "middle name"+id,
                "last name"+id,
                "mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8)
        );
    }

    public User generateUser(){
        return new User(
                "username",
                "first name",
                "middle name",
                "last name",
                "mail@gmail.com",
                LocalDate.of(1998,1,8)
        );
    }
    public User generateUserForList(String i){
        return new User(
                "username"+i,
                "first name"+i,
                "middle name"+i,
                "last name"+i,
                "mail"+i+"@gmail.com",
                LocalDate.of(1998,1,8)
        );
    }

    public UserDto generateUserDto(String id){
        return new UserDto(
                id,
                "username"+id,
                "first name"+id,
                "middle name"+id,
                "last name"+id,
                "mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8)
        );
    }
    public UserDto generateUserDto(){
        return new UserDto(
                "username",
                "first name",
                "middle name",
                "last name",
                "mail@gmail.com",
                LocalDate.of(1998,1,8)
        );
    }

    public List<User> generateUserListWithId(int size){
        return IntStream.range(0,size).mapToObj(i->generateUser(String.valueOf(i))).collect(Collectors.toList());
    }
    public List<UserDto> generateUserDtoListWithId(int size){
        return IntStream.range(0,size).mapToObj(i->generateUserDto(String.valueOf(i))).collect(Collectors.toList());
    }
    public List<User> generateUserList(int size){
        return IntStream.range(0,size).mapToObj(i->generateUserForList(String.valueOf(i))).collect(Collectors.toList());
    }
    public List<UserDto> generateUserDtoList(int size){
        return IntStream.range(0,size).mapToObj(i->generateUserDto()).collect(Collectors.toList());
    }


    // ENTRY

    public Entry generateEntry(String id, String title, String content,User user){
        return new Entry(
                id,
                user,
                title,
                content,
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
    public Entry generateEntry(String title,String content,User user){
        return new Entry(
                user,
                title,
                content,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public List<Entry> generateEntryListWithId(int size,User user){
        return IntStream.range(0,size).mapToObj(i->generateEntry(String.valueOf(i),"title"+i,"comment"+i,user)).collect(Collectors.toList());
    }
    public List<Entry> generateEntryList(int size,User user){
        return IntStream.range(0,size).mapToObj(i->generateEntry("title"+i,"comment"+i,user)).collect(Collectors.toList());
    }

    public EntryDto generateEntryDto(String id, String title, String content,UserDto user){
        return new EntryDto(
                id,
                title,
                content,
                user,
                LocalDateTime.now()
        );
    }
    public EntryDto generateEntryDto(String title, String content,UserDto user){
        return new EntryDto(
                title,
                content,
                user,
                LocalDateTime.now()
        );
    }

    public List<EntryDto> generateEntryDtoListWithId(int size,UserDto userDto){
        return IntStream.range(0,size).mapToObj(i->generateEntryDto(String.valueOf(i),"title"+i,"content"+i,userDto)).collect(Collectors.toList());
    }
    public List<EntryDto> generateEntryDtoList(int size,UserDto userDto){
        return IntStream.range(0,size).mapToObj(i->generateEntryDto("title"+i,"content"+i,userDto)).collect(Collectors.toList());
    }

    // COMMENT

    public Comment generateComment(User user,Entry entry,String comment,Boolean isUpdated){
        return new Comment(
                comment,
                entry,
                user,
                isUpdated
        );
    }

    public List<Comment> generateCommentList(int size, User user, Entry entry){
        return IntStream.range(0,size).mapToObj(i->generateComment(user,entry,"comment"+i,false)).collect(Collectors.toList());
    }

    public CommentDto generateCommentDto(UserDto userDto, EntryDto entryDto, String comment, Boolean isUpdated){
        return new CommentDto(
                comment,
                userDto,
                entryDto,
                isUpdated
        );
    }

    public List<CommentDto> generateCommentDtoList(int size,UserDto userDto,EntryDto entryDto){
        return IntStream.range(0,size).mapToObj(i->generateCommentDto(userDto,entryDto,"comment"+i,false)).collect(Collectors.toList());
    }
}
