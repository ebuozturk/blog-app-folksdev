package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.dto.entry.CreateEntryRequest;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.entry.UpdateEntryRequest;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.service.EntryService;
import com.ebuozturk.blogapp.utils.HateoasLinkSupporter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("v1/entry")
public class EntryController extends HateoasLinkSupporter {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping("{id}")
    public ResponseEntity<EntryDto> getEntryById(@PathVariable String id){
        EntryDto entryDto = entryService.getById(id);
        addLinkToEntryDto(entryDto);
        return ResponseEntity.ok(entryDto);
    }
    @GetMapping
    public ResponseEntity<List<EntryDto>> getAllEntries(){
        List<EntryDto> entryDtoList = entryService.getAllEntries();
        entryDtoList.forEach(this::addLinkToEntryDto);
        return ResponseEntity.ok(entryDtoList);
    }
    @GetMapping("/user")
    public ResponseEntity<List<EntryDto>> getAllEntriesByUserId(@RequestParam("id") String userId){
        List<EntryDto> entryDtoList = entryService.getAllEntriesByUserId(userId);
        entryDtoList.forEach(this::addLinkToEntryDto);
        return ResponseEntity.ok(entryDtoList);
    }
    @PostMapping
    public ResponseEntity<EntryDto> createEntry(@Valid @RequestBody CreateEntryRequest request){
        return new ResponseEntity<>(entryService.createEntry(request), HttpStatus.CREATED);
    }
    @PutMapping("{id}")
    public ResponseEntity<EntryDto> updateEntry(@PathVariable String id,@Valid @RequestBody UpdateEntryRequest request){
        return ResponseEntity.ok(entryService.updateEntry(id,request));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id){
        entryService.deleteEntry(id);
        return ResponseEntity.ok().build();
    }

}
