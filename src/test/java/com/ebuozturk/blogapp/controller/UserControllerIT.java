package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.IntegrationTestSupporter;
import com.ebuozturk.blogapp.dto.user.CreateUserRequest;
import com.ebuozturk.blogapp.dto.user.UpdateUserRequest;
import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;



class UserControllerIT  extends IntegrationTestSupporter {

    @Test
    public void testCreateUser_whenCreateUserRequestIsValid_itShouldReturnUserDto() throws Exception {
        CreateUserRequest request  = new CreateUserRequest(
                "username",
                "first name",
                "middle name",
                "last name",
                "email@gmail.com",
                LocalDate.of(1998,1,8)
        );

        this.mockMvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("username")))
                .andExpect(jsonPath("$.firstName", is("first name")))
                .andExpect(jsonPath("$.middleName", is("middle name")))
                .andExpect(jsonPath("$.lastName", is("last name")))
                .andExpect(jsonPath("$.email", is("email@gmail.com")));
//                .andExpect(jsonPath("$.birthDate", is(LocalDate.of(1998,1,8))));

        List<User> createdUser = userRepository.findAll();
        assertEquals(1,createdUser.size());
    }
    @Test
    public void testCreateUser_whenCreateUserRequestIsInvalid_itShouldNotCreateUserAndReturnBadRequest() throws Exception {
        CreateUserRequest request  = new CreateUserRequest(
                "username",
                "first name",
                "middle name",
                "",
                "email@gmail.com",
                LocalDate.of(1998,1,8)
        );

        this.mockMvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<User> createdUser = userRepository.findAll();
        assertEquals(0,createdUser.size());
    }

    @Test
    public void testUpdateUser_whenUserIdExistAndRequestIsValid_itShouldReturnUserDto() throws Exception{
        User user =  generateUser();
        user = userRepository.save(user);
        UpdateUserRequest request = new UpdateUserRequest(
                "username",
                "updated first name",
                "updated middle name",
                "last name",
                "updatedemail@gmail.com",
                LocalDate.of(1998,1,8)
        );


        this.mockMvc.perform(put("/v1/user/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(request.getUsername())))
                .andExpect(jsonPath("$.firstName", is(request.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(request.getMiddleName())))
                .andExpect(jsonPath("$.lastName", is(request.getLastName())))
                .andExpect(jsonPath("$.email", is(request.getEmail())));


    }

    @Test
    public void testUpdateUser_whenUserIdNotExistAndRequestIsValid_itShouldReturn404NotFound() throws Exception{

        UpdateUserRequest request = new UpdateUserRequest(
                "username",
                "updated first name",
                "updated middle name",
                "last name",
                "updatedemail@gmail.com",
                LocalDate.of(1998,1,8)
        );

        this.mockMvc.perform(put("/v1/user/"+"1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));

    }

    @Test
    public void testUpdateUser_whenUserIdExistAndRequestIsInvalid_itShouldReturnBadRequest() throws Exception{
        User user =  generateUser();
        user = userRepository.save(user);
        UpdateUserRequest request = new UpdateUserRequest(
                "username",
                "updated first name",
                "updated middle name",
                "",
                "updatedemail",
                LocalDate.of(1998,1,8)
        );


        this.mockMvc.perform(put("/v1/user/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testUpdateUser_whenUserIdNotExistAndRequestIsInvalid_itShouldReturn400BadRequest() throws Exception{

        UpdateUserRequest request = new UpdateUserRequest(
                "username",
                "updated first name",
                "updated middle name",
                "",
                "updatedemail",
                LocalDate.of(1998,1,8)
        );

        this.mockMvc.perform(put("/v1/user/"+"1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void testDeleteUser_whenUserIdExist_itShouldReturn200OK() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        this.mockMvc.perform(delete("/v1/user/"+user.getId())
                             .contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON))
                             .andExpect(status().isOk());
        User finalUser = user;
        assertThrows(UserNotFoundException.class,()-> userRepository.findById(finalUser.getId()).orElseThrow(()-> new UserNotFoundException("asda")));
    }

    @Test
    public void testDeleteUser_whenUserIdNotExist_itShouldReturn404NotFound() throws Exception{
        String id = "UUID123";
        this.mockMvc.perform(delete("/v1/user/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserById_whenUserIdExist_itShouldReturnUserDto() throws Exception{
        User user = generateUser();
        user = userRepository.save(user);

        this.mockMvc.perform(get("/v1/user/"+user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                             .andExpect(status().is2xxSuccessful())
                             .andExpect(jsonPath("$.id",is(user.getId())))
                             .andExpect(jsonPath("$.username",is(user.getUsername())))
                             .andExpect(jsonPath("$.firstName",is(user.getFirstName())))
                             .andExpect(jsonPath("$.middleName",is(user.getMiddleName())))
                             .andExpect(jsonPath("$.lastName",is(user.getLastName())))
                             .andExpect(jsonPath("$.email",is(user.getEmail())));

    }

    @Test
    public void testGetUserById_whenUserIdNotExist_itShouldReturn404NotFound() throws Exception{

        this.mockMvc.perform(get("/v1/user/"+"1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetAllUsers_itShouldReturnUserDtoList() throws Exception{
        List<User> userList = generateUserList(5);
        userList = userRepository.saveAll(userList);

        this.mockMvc.perform(get("/v1/user")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().is2xxSuccessful());
    }


}