package com.ebuozturk.blogapp.converter;

import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserDto convert(User user){
        return new UserDto(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate());
    }

    public List<UserDto> convert(List<User> users){
        return users.stream().map(this::convert).collect(Collectors.toList());
    }
}
