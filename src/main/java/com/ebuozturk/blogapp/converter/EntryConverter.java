package com.ebuozturk.blogapp.converter;

import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.entity.Entry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntryConverter {
    private final UserConverter userConverter;
    private final CommentConverter commentConverter;

    public EntryConverter(UserConverter userConverter, CommentConverter commentConverter) {
        this.userConverter = userConverter;
        this.commentConverter = commentConverter;
    }

    public EntryDto convert(Entry entry){
    return new EntryDto(
            entry.getId(),
            entry.getTitle(),
            entry.getContent(),
            userConverter.convert(entry.getUser()),
            entry.getCreatedDate()
            );
    }
    public List<EntryDto> convert(List<Entry> entries){
        return entries.stream().map(this::convert).collect(Collectors.toList());
    }
}
