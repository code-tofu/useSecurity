package tofu.code.useSecurity.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Override //TODO: IMPLEMENT USER DETAILS SERVICES
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
