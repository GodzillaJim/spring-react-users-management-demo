package com.godzillajim.tracker.repositories;

import com.godzillajim.tracker.domain.User;
import com.godzillajim.tracker.exceptions.TrackerAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private static final String SQL_CREATE = "INSERT INTO track_users(user_id, first_name, last_name, email, password) VALUES(NEXTVAL('track_users_seq'), ?,?,?,?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM track_users WHERE email = ?";
    private static final String SQL_FIND_BY_ID = "SELECT user_id, first_name, last_name, email, password FROM track_users WHERE user_id = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM track_users WHERE email = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Integer create(String firstName, String lastName, String email, String password) throws TrackerAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,firstName);
                ps.setString(2,lastName);
                ps.setString(3,email);
                ps.setString(4,hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("user_id");
        }catch(Exception e){
            throw new TrackerAuthException("Invalid details, failed to ccreate");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws TrackerAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper);
            if(!BCrypt.checkpw(password, user.getPassword()))
                throw new TrackerAuthException("Invalid email or password");
            return user;
        }catch(Exception e){
            throw new TrackerAuthException("Invalid email or password");
        }
    }

    @Override
    public Integer getCountByEmail(String email)throws TrackerAuthException{
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper);
    }
    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
       return new User(rs.getInt("user_id"),
               rs.getString("first_name"),
               rs.getString("last_name"),
               rs.getString("email"),
               rs.getString("password"));
    });
}
