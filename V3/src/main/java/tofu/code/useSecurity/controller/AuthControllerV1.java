package tofu.code.useSecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tofu.code.useSecurity.dto.CredentialsDTO;
import tofu.code.useSecurity.entity.UserDetailsImpl;
import tofu.code.useSecurity.enums.Authority;
import tofu.code.useSecurity.service.JWTService;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

import static tofu.code.useSecurity.enums.Authority.*;
@RestController
@Slf4j
@RequestMapping("/v1")
public class AuthControllerV1 {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signup/newUser")
    public ResponseEntity<String> signUpNewUser(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithAuthority(signupRequest,PROTECTED_AUTHORITY);
        log.info("New User Created with PROTECTED_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK"); //TODO: Better Response Implementation
    }

    @PostMapping("/signup/newUser/wri")
    public ResponseEntity<String> signUpNewUserWithWriteAuthority(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithAuthority(signupRequest,WRITE_AUTHORITY);
        log.info("New User Created with WRITE_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK: WRITE_AUTHORITY");  //TODO: Better Response Implementation
    }

    @PostMapping("/signup/newUser/del")
    public ResponseEntity<String> signUpNewUserWithDeleteAuthority(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithAuthority(signupRequest,DELETE_AUTHORITY);
        log.info("New User Created with DELETE_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK: DELETE_AUTHORITY");  //TODO: Better Response Implementation
    }

    public UserDetailsImpl saveNewUserWithAuthority(CredentialsDTO credentials, Authority authority){
        UserDetails newUser = UserDetailsImpl.builder()
                .username(credentials.getUsername()).password(passwordEncoder.encode(credentials.getPassword()))
                .enabled(true).accountNonLocked(true).accountNonExpired(true).credentialsNonExpired(true)
                .authorities(String.valueOf(authority))
                .build();
         return (UserDetailsImpl) userDetailsService.createNewUser(newUser);

    }
}

