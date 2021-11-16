package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.dto.entry.CreateEntryRequest;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.entry.UpdateEntryRequest;
import com.ebuozturk.blogapp.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/entry")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping("{id}")
    public ResponseEntity<EntryDto> getEntryById(@PathVariable String id){
        return ResponseEntity.ok(entryService.getById(id));
    }
    @GetMapping
    public ResponseEntity<List<EntryDto>> getAllEntries(){
        return ResponseEntity.ok(entryService.getAllEntries());
    }
    @GetMapping("/user")
    public ResponseEntity<List<EntryDto>> getAllEntriesByUserId(@RequestParam("id") String userId){
        return ResponseEntity.ok(entryService.getAllEntriesByUserId(userId));
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
