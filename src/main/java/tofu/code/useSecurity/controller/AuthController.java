package tofu.code.useSecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tofu.code.useSecurity.dto.SignInRequestDTO;
import tofu.code.useSecurity.dto.SignInResponseDTO;
import tofu.code.useSecurity.security.JWTService;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> signin(@RequestBody SignInRequestDTO signinRequest) throws JsonProcessingException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //TODO: FIX principal and authorities
        SignInResponseDTO response = new SignInResponseDTO(jwtService.generateToken(
                (String) authentication.getPrincipal(),
                Map<String,Object> authentication.getAuthorities())
                );
        return ResponseEntity.ok(response);
    }
}
