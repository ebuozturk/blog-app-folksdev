package com.ebuozturk.blogapp.service;

import com.ebuozturk.blogapp.converter.UserConverter;
import com.ebuozturk.blogapp.dto.user.CreateUserRequest;
import com.ebuozturk.blogapp.dto.user.UpdateUserRequest;
import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.UserNotFoundException;
import com.ebuozturk.blogapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public List<UserDto> getAllUsers(){
        return userConverter.convert(userRepository.findAll());
    }

    public UserDto getUserById(String id){
        return userConverter.convert(findById(id));
    }

    public UserDto createUser(CreateUserRequest request){

        return userConverter.convert(userRepository.save(new User(request.getUsername(),
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getEmail(),
                request.getBirthDate(),
                LocalDateTime.now()
                )));
    }

    public UserDto updateUser(String id, UpdateUserRequest request){
        User user = findById(id);
        User updateUser = new User(
                user.getId(),
                request.getUsername(),
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getEmail(),
                request.getBirthDate(),
                user.getCreatedDate(),
                LocalDateTime.now(),
                user.getEntries(),
                user.getComments()
        );
        return userConverter.convert(userRepository.save(updateUser));
    }
    public void deleteUser(String id) {
        User user = findById(id);
        userRepository.delete(user);
    }
    protected User findById(String id){
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User is not found by following id: "+id));
    }
    protected Boolean isUserExistById(String id){
        return userRepository.existsById(id);
    }

}
