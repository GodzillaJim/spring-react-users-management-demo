package com.godzillajim.tracker.repositories;

import com.godzillajim.tracker.domain.User;
import com.godzillajim.tracker.exceptions.TrackerAuthException;

public interface UserRepository {
    Integer create(String firstName, String lastName, String email, String password, String image) throws TrackerAuthException;
    User findByEmailAndPassword(String email, String password) throws TrackerAuthException;
    Integer getCountByEmail(String email);
    User findById(Integer userId);
}
