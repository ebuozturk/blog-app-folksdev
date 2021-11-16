package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.IntegrationTestSupporter;
import com.ebuozturk.blogapp.dto.entry.CreateEntryRequest;
import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.entry.UpdateEntryRequest;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.EntryNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EntryControllerIT extends IntegrationTestSupporter {


    @Test
    public void testCreateEntry_whenUserIdExistAndRequestIsValid_itShouldReturnEntryDto() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);
        CreateEntryRequest request = new CreateEntryRequest(
                "title",
                "content",
                user.getId()
        );

        this.mockMvc.perform(post("/v1/entry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                            .andExpect(status().is2xxSuccessful())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.title",is(request.getTitle())))
                            .andExpect(jsonPath("$.content",is(request.getContent())))
                            .andExpect(jsonPath("$.user.id",is(user.getId())));

        List<Entry> entry = entryRepository.findAll();
        assertEquals(1,entry.size());
    }

    @Test
    public void testCreateEntry_whenUserIdNotExistAndRequestIsValid_itShouldReturnNotFound() throws Exception{

        CreateEntryRequest request = new CreateEntryRequest(
                "title",
                "content",
                "user id"
        );

        this.mockMvc.perform(post("/v1/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound());

        List<Entry> entry = entryRepository.findAll();
        assertEquals(0,entry.size());
    }

    @Test
    public void testCreateEntry_whenUserIdExistAndRequestIsInvalid_itShouldReturnBadRequest() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        CreateEntryRequest request = new CreateEntryRequest(
                "",
                "",
                user.getId()
        );

        this.mockMvc.perform(post("/v1/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<Entry> entry = entryRepository.findAll();
        assertEquals(0,entry.size());
    }

    @Test
    public void testCreateEntry_whenUserIdNotExistAndRequestIsInvalid_itShouldReturnBadRequest() throws Exception{

        CreateEntryRequest request = new CreateEntryRequest(
                "",
                "",
                "user id"
        );

        this.mockMvc.perform(post("/v1/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        List<Entry> entry = entryRepository.findAll();
        assertEquals(0,entry.size());
    }

    @Test
    public void testUpdateEntry_whenEntryIdExistAndUserIdExistAndRequestIsValid_itShouldReturnEntryDto() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);
        Entry entry = new Entry(
                user,
                "title",
                "content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        entry = entryRepository.save(entry);
        String entryId = entry.getId();

        UpdateEntryRequest request = new UpdateEntryRequest(
                "updated title",
                "updated content",
                user.getId()
        );

        this.mockMvc.perform(put("/v1/entry/"+entryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(entryId)))
                .andExpect(jsonPath("$.title",is(request.getTitle())))
                .andExpect(jsonPath("$.content",is(request.getContent())))
                .andExpect(jsonPath("$.user.id",is(user.getId())));

    }

    @Test
    public void testUpdateEntry_whenEntryIdNotExistAndUserIdExistAndRequestIsValid_itShouldReturnNotFound() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        UpdateEntryRequest request = new UpdateEntryRequest(
                "updated title",
                "updated content",
                user.getId()
        );

        this.mockMvc.perform(put("/v1/entry/"+"1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound());

    }

//    @Test
//    public void testUpdateEntry_whenEntryIdExistAndUserIdNotExistAndRequestIsValid_itShouldReturnNotFound() throws Exception{
//
//
//        UpdateEntryRequest request = new UpdateEntryRequest(
//                "updated title",
//                "updated content",
//                user.getId()
//        );
//
//        this.mockMvc.perform(put("/v1/entry/"+"1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
//                .andExpect(status().isNotFound());
//
//    }

    @Test
    public void testDeleteEntry_whenEntryIdExist_itShouldReturn200OK() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = new Entry(
                user,
                "title",
                "content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        entry = entryRepository.save(entry);

        String entryId = entry.getId();

        this.mockMvc.perform(delete("/v1/entry/"+entryId)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());

        assertThrows(EntryNotFoundException.class,()-> entryService.getById(entryId));
    }

    @Test
    public void testDeleteEntry_whenEntryIdNotExist_itShouldReturnNotFound() throws Exception{

        this.mockMvc.perform(delete("/v1/entry/"+"entryId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEntryById_whenEntryIdExist_itShouldReturnEntryDto() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = new Entry(
                user,
                "title",
                "content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        entry = entryRepository.save(entry);

        String entryId = entry.getId();

        this.mockMvc.perform(get("/v1/entry/"+entryId))
                            .andExpect(status().is2xxSuccessful())
                            .andExpect(jsonPath("$.id",is(entryId)))
                            .andExpect(jsonPath("$.title",is("title")))
                            .andExpect(jsonPath("$.content",is("content")))
                            .andExpect(jsonPath("$.user.id",is(user.getId())));
//                            .andExpect(jsonPath("$.createdDate",is(entry.getCreatedDate().toString())));

    }

    @Test
    public void testGetEntryById_whenEntryIdNotExist_itShouldReturnNotFound() throws Exception{

        this.mockMvc.perform(get("/v1/entry/"+"entryId"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetAllEntries_itShouldReturnEntryDtoList() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        List<Entry> entries = generateEntryList(5,user);
        entries = entryRepository.saveAll(entries);
        List<EntryDto> entryDtos = entryConverter.convert(entries);

        MvcResult result = this.mockMvc.perform(get("/v1/entry"))
                            .andExpect(status().is2xxSuccessful()).andReturn();


    }

    @Test
    public void testGetAllEntriesByUserId_itShouldReturnEntryDtoList() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        List<Entry> entries = generateEntryList(5,user);
        entries = entryRepository.saveAll(entries);

        MvcResult result = this.mockMvc.perform(get("/v1/entry/user?id="+user.getId()))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

}