package com.ebuozturk.blogapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntryNotFoundException extends NotFoundException{
    public EntryNotFoundException(String message) {
        super(message);
    }
}
