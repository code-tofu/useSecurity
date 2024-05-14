package tofu.code.useSecurity.security;

public class JWTAuthenticationFilter {

    //TODO: Implement


    //ALTERNATIVE WAY TO EXTRACT BEARER
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
    
}
