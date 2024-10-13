package tofu.code.useSecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tofu.code.useSecurity.exception.CustomControllerException;

@RestController
public class ProtectedController {

    @GetMapping(path="/protected")
    public ResponseEntity<String> mustBeAuthenticated() {
        return ResponseEntity.ok("PROTECTED API OK");
    }

    @GetMapping(path="/write")
    public ResponseEntity<String> mustHaveWriteAuthority() {
        return ResponseEntity.ok("WRITE_AUTHORITY API OK");
    }

    @GetMapping(path="/delete")
    public ResponseEntity<String> mustHaveDeleteAuthority() {
        return ResponseEntity.ok("DELETE_AUTHORITY API OK");
    }


}
