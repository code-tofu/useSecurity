package tofu.code.useSecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tofu.code.useSecurity.dto.SignInRequestDTO;
import tofu.code.useSecurity.dto.SignInResponseDTO;
import tofu.code.useSecurity.dto.SignUpRequestDTO;
import tofu.code.useSecurity.dto.SignUpResponseDTO;
import tofu.code.useSecurity.service.JWTService;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signin") //TODO: FIX principal and authorities
    public ResponseEntity<SignInResponseDTO> signin(@RequestBody SignInRequestDTO signinRequest) throws JsonProcessingException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Map<String,Object> roleClaims = new HashMap<>;
        roleClaims.put("role",user.getAuthorities().get(0).toString());
        
        SignInResponseDTO response = new SignInResponseDTO(jwtService.generateToken(
                user.getUsername(), roleClaims),user.getAuthorities().get(0).toString());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup/newUser") //TODO: FIX principal and authorities. FIX Response
    public ResponseEntity<SignUpResponseDTO> signUpNewUser(@RequestBody SignUpRequestDTO signupRequest) throws JsonProcessingException {
        UserDetails newUser = userDetailsService.createNewUser(signupRequest);
        return ResponseEntity.ok(new SignUpResponseDTO());
    }
}
