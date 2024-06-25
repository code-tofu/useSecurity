package tofu.code.useSecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tofu.code.useSecurity.dto.CredentialsDTO;
import tofu.code.useSecurity.dto.SignInResponseDTO;
import tofu.code.useSecurity.entity.UserDetailsImpl;
import tofu.code.useSecurity.enums.Authority;
import tofu.code.useSecurity.service.JWTService;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

import static tofu.code.useSecurity.enums.Authority.*;
@RestController
@Slf4j
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> signin(@RequestBody CredentialsDTO signinRequest) throws JsonProcessingException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        log.info("User Authenticated:{}",user.getUserId());
        //V1 authorities implementation: Role Based Single String
        SignInResponseDTO response = new SignInResponseDTO(jwtService.generateToken(user),
                user.getAuthorities().get(0).toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup/newUser")
    public ResponseEntity<String> signUpNewUser(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithAuthority(signupRequest,PROTECTED_AUTHORITY);
        log.info("New User Created with PROTECTED_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK");
    }

    @PostMapping("/signup/newUser/wri")
    public ResponseEntity<String> signUpNewUserWithWriteAuthority(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithAuthority(signupRequest,WRITE_AUTHORITY);
        log.info("New User Created with WRITE_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK: WRITE_AUTHORITY");
    }

    @PostMapping("/signup/newUser/del")
    public ResponseEntity<String> signUpNewUserWithDeleteAuthority(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithAuthority(signupRequest,DELETE_AUTHORITY);
        log.info("New User Created with DELETE_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK: DELETE_AUTHORITY");
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

