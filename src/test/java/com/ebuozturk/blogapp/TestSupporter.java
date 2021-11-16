package com.ebuozturk.blogapp;

import com.ebuozturk.blogapp.dto.comment.CommentDto;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.entity.Comment;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupporter {

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

    public List<User> generateUserList(int size){
        return IntStream.range(0,size).mapToObj(i->generateUser(String.valueOf(i))).collect(Collectors.toList());
    }
    public List<UserDto> generateUserDtoList(int size){
        return IntStream.range(0,size).mapToObj(i->generateUserDto(String.valueOf(i))).collect(Collectors.toList());
    }

    //ENTRY

    public Entry generateEntry(String id,String title,String content){
        return new Entry(
                id,
                generateUser(id),
                title,
                content,
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
    public Entry generateEntry(String title,String content){
        return new Entry(
                generateUser("1"),
                title,
                content,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public List<Entry> generateEntryList(int size){
        return IntStream.range(0,size).mapToObj(i->generateEntry(String.valueOf(i),"title"+i,"comment"+i)).collect(Collectors.toList());
    }

    public EntryDto generateEntryDto(String id,String title,String content){
        return new EntryDto(
                id,
                title,
                content,
                generateUserDto(id),
                LocalDateTime.now()
        );
    }

    public List<EntryDto> generateEntryDtoList(int size){
        return IntStream.range(0,size).mapToObj(i->generateEntryDto(String.valueOf(i),"title"+i,"content"+i)).collect(Collectors.toList());
    }

    // Comment

    public Comment generateComment(String id,String userId,String entryId,String comment,Boolean isUpdated){
        return new Comment(
                id,
                comment,
                generateEntry(entryId,"title","content"),
                generateUser(userId),
                isUpdated
        );
    }
    public Comment generateComment(String userId,String entryId,String comment,Boolean isUpdated){
        return new Comment(
                comment,
                generateEntry(entryId,"title","content"),
                generateUser(userId),
                isUpdated
        );
    }

    public List<Comment> generateCommentList(int size){
        return IntStream.range(0,size).mapToObj(i->generateComment(String.valueOf(i),String.valueOf(i),String.valueOf(i),"comment"+i,false)).collect(Collectors.toList());
    }

    public CommentDto generateCommentDto(String id,String userId,String entryId,String comment,Boolean isUpdated){
        return new CommentDto(
                id,
                comment,
                generateUserDto(userId),
                generateEntryDto(entryId,"title","content"),
                isUpdated
        );
    }

    public List<CommentDto> generateCommentDtoList(int size){
        return IntStream.range(0,size).mapToObj(i->generateCommentDto(String.valueOf(i),String.valueOf(i),String.valueOf(i),"comment"+i,false)).collect(Collectors.toList());
    }

}
