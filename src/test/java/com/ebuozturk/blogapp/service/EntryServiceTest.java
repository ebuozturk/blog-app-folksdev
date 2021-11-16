package com.ebuozturk.blogapp.service;

import com.ebuozturk.blogapp.TestSupporter;
import com.ebuozturk.blogapp.converter.EntryConverter;
import com.ebuozturk.blogapp.dto.entry.CreateEntryRequest;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.entry.UpdateEntryRequest;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.EntryNotFoundException;
import com.ebuozturk.blogapp.exception.UserNotFoundException;
import com.ebuozturk.blogapp.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EntryServiceTest extends TestSupporter {

    private EntryRepository entryRepository;
    private EntryConverter entryConverter;
    private UserService userService;
    private EntryService entryService;

    @BeforeEach
    void setUp() {
        entryRepository = mock(EntryRepository.class);
        entryConverter = mock(EntryConverter.class);
        userService = mock(UserService.class);
        entryService = new EntryService(entryRepository,entryConverter,userService);

    }

    @Test
    public void testCreateEntry_whenUserExist_itShouldReturnEntryDto(){
        String id = "1";
        User user = generateUser(id);
        Entry entry = generateEntry("title","content");

        CreateEntryRequest request = new CreateEntryRequest(
                "title"+id,
                "content"+id,
                id
        );
        Entry savedEntry = generateEntry(id,"title","content");
        EntryDto entryDto = generateEntryDto(id,"title","content");

        when(userService.findById(id)).thenReturn(user);
        when(entryRepository.save(entry)).thenReturn(savedEntry);
        when(entryConverter.convert(savedEntry)).thenReturn(entryDto);

        EntryDto result = entryService.createEntry(request);

        assertEquals(result,entryDto);

        verify(userService).findById(id);
        verify(entryRepository).save(entry);
        verify(entryConverter).convert(savedEntry);

    }

    @Test
    public void testCreateEntry_whenUserNotExist_itShouldThrowUserNotFoundException(){
        String id = "1";
        CreateEntryRequest request = new CreateEntryRequest(
                "title"+id,
                "content"+id,
                id
        );

        when(userService.findById(id)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,()->entryService.createEntry(request));

        verify(userService).findById(id);
        verifyNoInteractions(entryRepository);
        verifyNoInteractions(entryConverter);

    }

    @Test
    public void testUpdateEntry_whenEntryExist_itShouldReturnEntryDto(){
        String id = "1";
        Entry entry = generateEntry(id,"title","content");
        UpdateEntryRequest request = new UpdateEntryRequest(
                "updated title",
                "updated content",
                id
        );
        Entry updateEntry = generateEntry(id,"updated title","updated content");
        EntryDto updatedEntryDto = generateEntryDto(id,"updated title","updated content");

        when(entryRepository.findById(id)).thenReturn(Optional.of(entry));
        when(entryRepository.save(updateEntry)).thenReturn(updateEntry);
        when(entryConverter.convert(updateEntry)).thenReturn(updatedEntryDto);

        EntryDto result = entryService.updateEntry(id,request);

        assertEquals(result,updatedEntryDto);

        verify(entryRepository).findById(id);
        verify(entryRepository).save(updateEntry);
        verify(entryConverter).convert(updateEntry);


    }
    @Test
    public void testUpdateEntry_whenEntryNotExist_itShouldThrowEntryNotFoundException(){
        String id = "1";
        UpdateEntryRequest request = new UpdateEntryRequest(
                "updated title",
                "updated content",
                id
        );

        when(entryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, ()->entryService.updateEntry(id,request));

        verify(entryRepository).findById(id);
        verifyNoMoreInteractions(entryRepository);
        verifyNoInteractions(entryConverter);

    }

    @Test
    public void testDeleteEntry_whenEntryExist_itShouldReturnVoid(){
        String id = "1";
        Entry entry = generateEntry(id,"title","content");

        when(entryRepository.findById(id)).thenReturn(Optional.of(entry));
        doNothing().when(entryRepository).delete(entry);

        entryService.deleteEntry(id);

        verify(entryRepository).findById(id);
        verify(entryRepository).delete(entry);
    }

    @Test
    public void testDeleteEntry_whenEntryNotExist_itShouldThrowEntryNotFoundException(){
        String id = "1";

        when(entryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class,()->entryService.deleteEntry(id));

        verify(entryRepository).findById(id);
        verifyNoMoreInteractions(entryRepository);
    }

    @Test
    public void testGetAllEntries_itShouldReturnEntryDtoList(){
        List<Entry> entryList = generateEntryList(4);
        List<EntryDto> entryDtoList = generateEntryDtoList(4);

        when(entryRepository.findAll()).thenReturn(entryList);
        when(entryConverter.convert(entryList)).thenReturn(entryDtoList);

        List<EntryDto> result = entryService.getAllEntries();

        assertEquals(result,entryDtoList);

        verify(entryRepository).findAll();
        verify(entryConverter).convert(entryList);

    }

    @Test
    public void testGetAllEntriesByUserId_itShouldReturnEntryDtoList(){
        String userId = "1";
        List<Entry> entryList = generateEntryList(4);
        List<EntryDto> entryDtoList = generateEntryDtoList(4);

        when(entryRepository.findByUser_id(userId)).thenReturn(entryList);
        when(entryConverter.convert(entryList)).thenReturn(entryDtoList);

        List<EntryDto> result = entryService.getAllEntriesByUserId(userId);

        assertEquals(result,entryDtoList);

        verify(entryRepository).findByUser_id(userId);
        verify(entryConverter).convert(entryList);

    }

    @Test
    public void testGetById_whenEntryExist_itShouldReturnEntryDto(){
        String id = "1";
        Entry entry = generateEntry(id,"title","content");
        EntryDto entryDto = generateEntryDto(id,"title","content");

        when(entryRepository.findById(id)).thenReturn(Optional.of(entry));
        when(entryConverter.convert(entry)).thenReturn(entryDto);

        EntryDto result = entryService.getById(id);

        assertEquals(result,entryDto);

        verify(entryRepository).findById(id);
        verify(entryConverter).convert(entry);

    }
    @Test
    public void testGetById_whenEntryNotExist_itShouldThrowEntryNotFoundException(){
        String id = "1";

        when(entryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class,()->entryService.getById(id));

        verify(entryRepository).findById(id);
        verifyNoInteractions(entryConverter);

    }

    @Test
    public void testFindById_whenEntryIdExist_itShouldReturnEntry(){
        String id = "1";
        Entry entry = generateEntry(id,"title","content");

        when(entryRepository.findById(id)).thenReturn(Optional.of(entry));

        Entry result = entryService.findById(id);

        assertEquals(result,entry);

        verify(entryRepository).findById(id);
    }

    @Test
    public void testFindById_whenEntryIdNotExist_itShouldThrowEntryNotFoundException(){
        String id = "1";

        when(entryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class,()->entryService.findById(id));

        verify(entryRepository).findById(id);

    }
    @Test
    public void testFindByUserId_itShouldReturnEntryList(){
        String userId="1";
        List<Entry> entries = generateEntryList(4);

        when(entryRepository.findByUser_id(userId)).thenReturn(entries);

        List<Entry> result = entryService.findByUserId(userId);

        assertEquals(result,entries);

        verify(entryRepository).findByUser_id(userId);

    }
}