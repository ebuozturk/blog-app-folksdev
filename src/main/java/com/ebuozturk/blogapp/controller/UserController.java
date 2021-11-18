package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.dto.entry.EntryDto;
import com.ebuozturk.blogapp.dto.user.CreateUserRequest;
import com.ebuozturk.blogapp.dto.user.UpdateUserRequest;
import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.service.UserService;
import com.ebuozturk.blogapp.utils.HateoasLinkSupporter;
import org.apache.coyote.Response;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/user")
public class UserController extends HateoasLinkSupporter {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> userDtoList = userService.getAllUsers();
        userDtoList.forEach(this::addLinkToUserDto);
        return ResponseEntity.ok(userDtoList);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id){
        UserDto userDto = userService.getUserById(id);
        addLinkToUserDto(userDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request){
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id,@Valid @RequestBody UpdateUserRequest request){
        return ResponseEntity.ok(userService.updateUser(id,request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
