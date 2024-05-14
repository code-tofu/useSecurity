package tofu.code.useSecurity.controller;

@RestController
public class ProtectedController {

    @GetMapping(path="/protected")
    public ResponseEntity<String> mustBeAuthenticated() {
        return ResponseEntity.ok("PROTECTED API OK");
    }

}
