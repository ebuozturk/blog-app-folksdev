package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.IntegrationTestSupporter;
import com.ebuozturk.blogapp.dto.comment.CreateCommentRequest;
import com.ebuozturk.blogapp.dto.comment.UpdateCommentRequest;
import com.ebuozturk.blogapp.entity.Comment;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

class CommentControllerIT extends IntegrationTestSupporter {

    @Test
    public void testGetCommentByUserIdAndEntryId_itShouldReturnCommentDtoList() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        this.mockMvc.perform(get("/v1/comment?userId="+user.getId()+"&entryId="+entry.getId()))
                                .andExpect(status().is2xxSuccessful())
                                .andExpect(jsonPath("$.[0].id",is(comment.getId())));
    }


    @Test
    public void testGetCommentByUserId_itShouldReturnCommentDtoList() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        this.mockMvc.perform(get("/v1/comment/user?id="+user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].id",is(comment.getId())));
    }

    @Test
    public void testGetCommentById_whenCommentIdExist_itShouldReturnCommentDto() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        this.mockMvc.perform(get("/v1/comment/"+comment.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id",is(comment.getId())))
                .andExpect(jsonPath("$.comment",is(comment.getComment())));
    }

    @Test
    public void testGetCommentById_whenCommentIdNotExist_itShouldReturnNotFound() throws Exception{

        this.mockMvc.perform(get("/v1/comment/"+"1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommentBuEntryId_itShouldReturnCommentDtoList() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        this.mockMvc.perform(get("/v1/comment/entry?id="+entry.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].id",is(comment.getId())));
    }

    @Test
    public void testCreateComment_whenUserIdAndEntryIdExistAndRequestIsValid_itShouldReturnCommentDto() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        CreateCommentRequest request = new CreateCommentRequest(
                "comment",
                user.getId(),
                entry.getId()
        );

        this.mockMvc.perform(post("/v1/comment")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                            .andExpect(status().is2xxSuccessful())
                            .andExpect(jsonPath("$.comment",is(request.getComment())))
                            .andExpect(jsonPath("$.user.id",is(user.getId())))
                            .andExpect(jsonPath("$.entry.id",is(entry.getId())));

        assertEquals(1,commentRepository.findAll().size());
    }

    @Test
    public void testCreateComment_whenUserIdAndEntryIdExistAndRequestIsInvalid_itShouldReturnBadRequest() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        CreateCommentRequest request = new CreateCommentRequest(
                "",
                user.getId(),
                entry.getId()
        );

        this.mockMvc.perform(post("/v1/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(0,commentRepository.findAll().size());
    }

    @Test
    public void testCreateComment_whenUserIdNotExistAndEntryIdExistAndRequestIsValid_itShouldReturnUserNotFound() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        CreateCommentRequest request = new CreateCommentRequest(
                "comment",
                "notExistId",
                entry.getId()
        );

        this.mockMvc.perform(post("/v1/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertEquals(0,commentRepository.findAll().size());
    }

    @Test
    public void testCreateComment_whenUserIdAndEntryIdNotExistAndRequestIsValid_itShouldReturnEntryNotFound() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);


        CreateCommentRequest request = new CreateCommentRequest(
                "comment",
                user.getId(),
                "notExistId"
        );

        this.mockMvc.perform(post("/v1/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertEquals(0,commentRepository.findAll().size());
    }

    @Test
    public void testUpdateComment_whenCommentIdExistAndRequestIsValid_itShouldReturnCommentDto() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        UpdateCommentRequest request = new UpdateCommentRequest(
                "updated comment"
        );

        this.mockMvc.perform(put("/v1/comment/"+comment.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                             .andExpect(status().is2xxSuccessful())
                             .andExpect(jsonPath("$.comment",is(request.getComment())))
                             .andExpect(jsonPath("$.isUpdated",is(true)));

    }

    @Test
    public void testUpdateComment_whenCommentIdNotExistAndRequestIsValid_itShouldReturnNotFound() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        UpdateCommentRequest request = new UpdateCommentRequest(
                "updated comment"
        );

        this.mockMvc.perform(put("/v1/comment/"+"notExistId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testUpdateComment_whenCommentIdExistAndRequestIsInvalid_itShouldReturnBadRequest() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        UpdateCommentRequest request = new UpdateCommentRequest(
                ""
        );

        this.mockMvc.perform(put("/v1/comment/"+comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testDeleteComment_whenCommentIdExist_itShouldReturn200Ok() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        this.mockMvc.perform(delete("/v1/comment/"+comment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testDeleteComment_whenCommentIdNotExist_itShouldReturnNotFound() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        Entry entry = generateEntry("title","content",user);
        entry = entryRepository.save(entry);

        Comment comment = generateComment(user,entry,"comment",false);
        comment = commentRepository.save(comment);

        this.mockMvc.perform(delete("/v1/comment/"+"notExistId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        assertEquals(1,commentRepository.findAll().size());

    }

}