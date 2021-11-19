package com.ebuozturk.blogapp.converter;

import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.entity.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntryConverter {
    private final UserConverter userConverter;

    public EntryConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
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
