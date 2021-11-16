package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.dto.user.CreateUserRequest;
import com.ebuozturk.blogapp.dto.user.UpdateUserRequest;
import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserById(id));
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
