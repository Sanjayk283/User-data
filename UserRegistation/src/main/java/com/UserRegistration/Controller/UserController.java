package com.UserRegistration.Controller;

import com.UserRegistration.Repo.UserRepository;
import com.UserRegistration.config.JwtTokenUtil;
import com.UserRegistration.modal.UserInfo;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.UserRegistration.RequestPayload.UserRequest;
import com.UserRegistration.Service.UserService;
import com.UserRegistration.config.APIResponse;
import com.UserRegistration.dto.Auth;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "User Services")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody UserRequest userRequest) {
        APIResponse response = userService.addUser(userRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public String login(@RequestBody Auth auth, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
            Optional<UserInfo> userInfo1;
              userInfo1= userRepository.findByUserName(auth.getUsername().trim());
              UserInfo userInfo = userInfo1.get();
              if (userInfo!=null){
            if (authentication.isAuthenticated()) {
                
                return jwtTokenUtil.generateToken(userInfo, request); // 60 minutes expiry
            } else {
                throw new UsernameNotFoundException("Invalid user credentials!");
            }
              }else {
                  throw new UsernameNotFoundException("User not found !");

              }
        } catch (Exception e) {
            log.error("Login failed", e);
            return "Invalid user credentials!";
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllList() {
        APIResponse response = userService.getAllList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllpagination")
    public ResponseEntity<Object> getAllListPagination(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy) {

        APIResponse response = userService.getAllListpagination(pageNumber, pageSize, sortBy);
        return ResponseEntity.ok(response);
    }
}
