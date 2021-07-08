package com.godzillajim.tracker.services;

import com.godzillajim.tracker.domain.User;
import com.godzillajim.tracker.exceptions.TrackerAuthException;

public interface UserService {
    User validateUser(String email, String password) throws TrackerAuthException;
    User registerUser(String firstName, String lastName, String email, String password, String image) throws TrackerAuthException;
    String getImage(Integer userId) throws TrackerAuthException;
    User getProfile(Integer userId);
}
