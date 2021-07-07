package com.godzillajim.tracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TrackerAuthException extends RuntimeException{
    public TrackerAuthException(String message){
        super(message);
    }
}
