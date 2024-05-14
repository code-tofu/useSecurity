package tofu.code.useSecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping(path="/")
    public ResponseEntity<String> publicLanding() {
        return ResponseEntity.ok("PUBLIC API OK");
    }

}
