package tofu.code.useSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tofu.code.useSecurity.service.UserDetailsServiceImpl;

@RestController
public class PublicController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping(path="/")
    public ResponseEntity<String> publicLanding() {
        return ResponseEntity.ok("PUBLIC API OK");
    }



}
