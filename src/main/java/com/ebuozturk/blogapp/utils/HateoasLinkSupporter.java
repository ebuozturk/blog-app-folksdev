package com.ebuozturk.blogapp.utils;

import com.ebuozturk.blogapp.controller.CommentController;
import com.ebuozturk.blogapp.controller.EntryController;
import com.ebuozturk.blogapp.controller.UserController;
import com.ebuozturk.blogapp.dto.comment.CommentDto;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.user.UserDto;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasLinkSupporter {

    public void addLinkToUserDto(UserDto userDto){
        Link userDtoLink = linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withSelfRel();
        userDto.add(userDtoLink);
    }
    public void addLinkToEntryDto(EntryDto entryDto){
        Link entryDtoLink = linkTo(methodOn(EntryController.class).getEntryById(entryDto.getId())).withSelfRel();
        entryDto.add(entryDtoLink);
        addLinkToUserDto(entryDto.getUser());
    }
    public void addLinkToCommentDto(CommentDto commentDto){
        Link commentDtoLink = linkTo(methodOn(CommentController.class).getCommentById(commentDto.getId())).withSelfRel();
        commentDto.add(commentDtoLink);
        addLinkToUserDto(commentDto.getUser());
        addLinkToEntryDto(commentDto.getEntry());
    }
}
