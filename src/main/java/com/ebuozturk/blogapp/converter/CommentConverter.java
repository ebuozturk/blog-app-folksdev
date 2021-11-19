package com.ebuozturk.blogapp.converter;

import com.ebuozturk.blogapp.dto.comment.CommentDto;
import com.ebuozturk.blogapp.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {

    private final UserConverter userConverter;
    private final EntryConverter entryConverter;

    public CommentConverter(UserConverter userConverter, EntryConverter entryConverter) {
        this.userConverter = userConverter;
        this.entryConverter = entryConverter;
    }

    public CommentDto convert(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                userConverter.convert(comment.getUser()),
                entryConverter.convert(comment.getEntry()),
                comment.isUpdated()
        );
    }
    public List<CommentDto> convert(List<Comment> comments){
        return comments.stream().map(this::convert).collect(Collectors.toList());
    }
}
