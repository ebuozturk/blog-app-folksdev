package com.ebuozturk.blogapp.service;

import com.ebuozturk.blogapp.converter.EntryConverter;
import com.ebuozturk.blogapp.dto.entry.CreateEntryRequest;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.entry.UpdateEntryRequest;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.EntryNotFoundException;
import com.ebuozturk.blogapp.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EntryService {
    private final EntryRepository entryRepository;
    private final EntryConverter entryConverter;
    private final UserService userService;

    public EntryService(EntryRepository entryRepository, EntryConverter entryConverter, UserService userService) {
        this.entryRepository = entryRepository;
        this.entryConverter = entryConverter;
        this.userService = userService;
    }

    public List<EntryDto> getAllEntries(){
        return entryConverter.convert(entryRepository.findAll());
    }
    public List<EntryDto> getAllEntriesByUserId(String userId){
        return entryConverter.convert(entryRepository.findByUser_id(userId));
    }

    public EntryDto getById(String id){
        return entryConverter.convert(findById(id));
    }

    public EntryDto createEntry(CreateEntryRequest request){
        User user = userService.findById(request.getUserId());
        Entry entry = new Entry(
                user,
                request.getTitle(),
                request.getContent(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return entryConverter.convert(entryRepository.save(entry));
    }

    public EntryDto updateEntry(String id,UpdateEntryRequest request){
        Entry entry = findById(id);
        Entry updateEntry = new Entry(
                entry.getId(),
                entry.getUser(),
                request.getTitle(),
                request.getContent(),
                entry.getComments(),
                entry.getCreatedDate(),
                LocalDateTime.now()
        );
        return entryConverter.convert(entryRepository.save(updateEntry));
    }

    public void deleteEntry(String entryId){
        Entry entry = findById(entryId);
        entryRepository.delete(entry);
    }

    protected List<Entry> findByUserId(String userId){
        return entryRepository.findByUser_id(userId);
    }

    protected Entry findById(String entryId) {
        return entryRepository.findById(entryId).orElseThrow(()->
                new EntryNotFoundException("The entry couldn't be found by following id: "+entryId));
    }
}
