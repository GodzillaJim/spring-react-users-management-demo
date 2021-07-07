package com.godzillajim.tracker.services;

import com.godzillajim.tracker.domain.User;
import com.godzillajim.tracker.exceptions.TrackerAuthException;
import com.godzillajim.tracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImp implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws TrackerAuthException {
        if(email != null) email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws TrackerAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(email != null) email = email.toLowerCase();
        if(!pattern.matcher(email).matches())
            throw new TrackerAuthException("Invalid email format");
        Integer count = userRepository.getCountByEmail(email);
        if(count > 0)
            throw new TrackerAuthException("Email already in use");
        Integer userId = userRepository.create(firstName,lastName,email,password);
        return userRepository.findById(userId);
    }
}
