package com.ebuozturk.blogapp.service;

import com.ebuozturk.blogapp.TestSupporter;
import com.ebuozturk.blogapp.converter.UserConverter;
import com.ebuozturk.blogapp.dto.user.CreateUserRequest;
import com.ebuozturk.blogapp.dto.user.UpdateUserRequest;
import com.ebuozturk.blogapp.dto.user.UserDto;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.UserNotFoundException;
import com.ebuozturk.blogapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest extends TestSupporter {

    private UserConverter userConverter;
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userConverter = mock(UserConverter.class);
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository,userConverter);
    }

    @Test
    public void testGetAllUsers_itShouldReturnUserDtoList(){
        List<User> userList = generateUserList(3);
        List<UserDto> userDtoList = generateUserDtoList(3);
        when(userRepository.findAll()).thenReturn(userList);
        when(userConverter.convert(userList)).thenReturn(userDtoList);

        List<UserDto> result =  userService.getAllUsers();

        assertEquals(result,userDtoList);

        verify(userRepository).findAll();
        verify(userConverter).convert(userList);
    }

    @Test
    public void testGetUserById_WhenIdExist_itShouldReturnUserDto(){
        String id ="1";
        User user = generateUser(id);
        UserDto userDto = generateUserDto(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userConverter.convert(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(id);

        assertEquals(result,userDto);

        verify(userRepository).findById(id);
        verify(userConverter).convert(user);
    }

    @Test
    public void testGetUserById_WhenIdNotExist_itShouldThrowUserNotException(){
        String id ="1";
        User user = generateUser(id);
        UserDto userDto = generateUserDto(id);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> userService.getUserById(id));

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userConverter);
    }

    @Test
    public void testCreateUser_itShouldReturnUserDto(){
        String id = "1";
        CreateUserRequest request = new CreateUserRequest(
                "username"+id,
                "first name"+id,
                "middle name"+id,
                "last name"+id,
                "mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8)
        );
        User user = new User(
                "username"+id,
                "first name"+id,
                "middle name"+id,
                "last name"+id,
                "mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8),
                LocalDateTime.now()
        );

        User savedUser = generateUser(id);
        UserDto userDto = generateUserDto(id);

        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverter.convert(savedUser)).thenReturn(userDto);

        UserDto result = userService.createUser(request);

        assertEquals(result,userDto);

        verify(userRepository).save(user);
        verify(userConverter).convert(savedUser);
    }

    @Test
    public void testUpdateUser_whenUserExist_itShouldReturnUserDto(){
        String id = "1";

        User currentUser = generateUser(id);

        UpdateUserRequest request = new UpdateUserRequest(
                "updated username"+id,
                "updated first name"+id,
                "updated middle name"+id,
                "updated last name"+id,
                "updated mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8)
        );
        User updateUser = new User(
                currentUser.getId(),
                request.getUsername(),
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getEmail(),
                request.getBirthDate(),
                currentUser.getCreatedDate(),
                LocalDateTime.now(),
                currentUser.getEntries(),
                currentUser.getComments()
        );
        User updatedUser = new User(
                id,
                "updated username"+id,
                "updated first name"+id,
                "updated middle name"+id,
                "updated last name"+id,
                "updated mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8)
        );

        UserDto userDto = new UserDto(
                id,
                "updated username"+id,
                "updated first name"+id,
                "updated middle name"+id,
                "updated last name"+id,
                "updated mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8)
        );


        when(userRepository.findById(id)).thenReturn(Optional.of(currentUser));
        when(userRepository.save(updateUser)).thenReturn(updatedUser);
        when(userConverter.convert(updatedUser)).thenReturn(userDto);

        UserDto result = userService.updateUser(id,request);

        assertEquals(result,userDto);

        verify(userRepository).findById(id);
        verify(userRepository).save(updateUser);
        verify(userConverter).convert(updatedUser);
    }

    @Test
    public void testUpdateUser_whenUserNotExist_itShouldThrowUserNotFoundException(){
        String id = "1";

        UpdateUserRequest request = new UpdateUserRequest(
                "updated username"+id,
                "updated first name"+id,
                "updated middle name"+id,
                "updated last name"+id,
                "updated mail"+id+"@gmail.com",
                LocalDate.of(1998,1,8)
        );

        when(userRepository.findById(id)).thenReturn(Optional.empty());

       assertThrows(UserNotFoundException.class,()->userService.updateUser(id,request));

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userConverter);
    }

    @Test
    public void testDeleteUser_whenUserExist_itShouldReturnVoid(){
        String id = "1";
        User user = generateUser(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(id);

        verify(userRepository).findById(id);
        verify(userRepository).delete(user);
    }

    @Test
    public void testDeleteUser_whenUserNotExist_itShouldThrowUserNotFoundException(){
        String id = "1";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.deleteUser(id));

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindById_whenUserExist_itShouldReturnUser(){
        String id = "1";
        User user = generateUser(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.findById(id);

        assertEquals(result,user);

        verify(userRepository).findById(id);
    }

    @Test
    public void testFindById_whenUserNotExist_itShouldThrowUserNotFoundException(){
        String id = "1";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.findById(id));

        verify(userRepository).findById(id);
    }

    @Test
    public void testIsUserExistById_whenUserExist_itShouldReturnTrue(){
        String id = "1";
        when(userRepository.existsById(id)).thenReturn(true);

        Boolean result =  userService.isUserExistById(id);

        assertEquals(result,true);

        verify(userRepository).existsById(id);
    }
    @Test
    public void testIsUserExistById_whenUserNotExist_itShouldReturnFalse(){
        String id = "1";
        when(userRepository.existsById(id)).thenReturn(false);

        Boolean result =  userService.isUserExistById(id);

        assertEquals(result,false);

        verify(userRepository).existsById(id);
    }
}