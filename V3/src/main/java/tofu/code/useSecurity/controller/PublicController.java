package tofu.code.useSecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tofu.code.useSecurity.dto.CredentialsDTO;
import tofu.code.useSecurity.dto.AuthResponseDTO;
import tofu.code.useSecurity.entity.UserDetailsImpl;
import tofu.code.useSecurity.exception.CustomControllerException;
import tofu.code.useSecurity.service.JWTService;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

@RestController
@Slf4j
public class PublicController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(path="/")
    public ResponseEntity<String> publicLanding() {
        return ResponseEntity.ok("PUBLIC API OK");
    }

    @GetMapping(path="/except")
    public ResponseEntity<String> triggerException() throws Exception {
        throw new CustomControllerException("EXCEPTION THROWN");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EXCEPTION NOT THROWN");
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> signin(@RequestBody CredentialsDTO signinRequest) throws JsonProcessingException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        log.info("User Authenticated:{}",user.getUserId());
        //V1 authorities implementation: Role Based Single String
        AuthResponseDTO response = new AuthResponseDTO(jwtService.generateToken(user), jwtService.generateRefreshToken(user),
                String.valueOf(user.getRole()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh() throws JsonProcessingException {

        return ResponseEntity.ok();
    }

}
