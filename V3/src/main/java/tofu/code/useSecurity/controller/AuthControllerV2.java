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
import tofu.code.useSecurity.enums.Role;
import tofu.code.useSecurity.service.JWTService;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

import static tofu.code.useSecurity.enums.Role.*;

@RestController
@Slf4j
@RequestMapping("/v2")
public class AuthControllerV2 {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signup/newUser/creator")
    public ResponseEntity<String> signUpNewUserCreator(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithRole(signupRequest,CREATOR);
        log.info("New User Created with PROTECTED_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK"); //TODO: Better Response Implementation
    }

    @PostMapping("/signup/newUser/viewer")
    public ResponseEntity<String> signUpNewUserWithWriteAuthority(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithRole(signupRequest,VIEWER);
        log.info("New User Created with WRITE_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK: VIEWER");  //TODO: Better Response Implementation
    }

    @PostMapping("/signup/newUser/admin")
    public ResponseEntity<String> signUpNewUserADMIN(@RequestBody CredentialsDTO signupRequest) throws JsonProcessingException {
        UserDetailsImpl savedUser = saveNewUserWithRole(signupRequest,ADMIN);
        log.info("New User Created with DELETE_AUTHORITY:{}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("SIGN UP OK: ADMIN");  //TODO: Better Response Implementation
    }

    public UserDetailsImpl saveNewUserWithRole(CredentialsDTO credentials, Role role){
        UserDetails newUser = UserDetailsImpl.builder()
                .username(credentials.getUsername()).password(passwordEncoder.encode(credentials.getPassword()))
                .enabled(true).accountNonLocked(true).accountNonExpired(true).credentialsNonExpired(true)
                .role(role)
                .build();
         return (UserDetailsImpl) userDetailsService.createNewUser(newUser);

    }
}

