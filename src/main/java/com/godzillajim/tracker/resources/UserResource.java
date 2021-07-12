package com.godzillajim.tracker.resources;

import com.godzillajim.tracker.Constants;
import com.godzillajim.tracker.domain.User;
import com.godzillajim.tracker.exceptions.TrackerAuthException;
import com.godzillajim.tracker.services.FileStorageService;
import com.godzillajim.tracker.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.validateUser(email, password);
        return new ResponseEntity<>(generateJWTToken(user),HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap){
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String image = (String) userMap.get("image");
        User user = userService.registerUser(firstName, lastName, email, password, image);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }
    @GetMapping("/profile")
    public User getProfile(HttpServletRequest request){
        int userId = (Integer) request.getAttribute("userId");
        return userService.getProfile(userId);
    }
    @PostMapping("/file")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try{
            if(file == null){
                throw new TrackerAuthException("Please provide file");
            }
            return fileStorageService.storeFile(file);
        }catch (Exception e){
            System.out.println(e);
            throw new TrackerAuthException(e.getMessage());
        }
    }
    @GetMapping("")
    private Map<String, String> generateJWTToken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId())
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
