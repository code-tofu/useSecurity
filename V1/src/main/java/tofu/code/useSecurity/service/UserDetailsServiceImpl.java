package tofu.code.useSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tofu.code.useSecurity.dto.SignUpRequestDTO;
import tofu.code.useSecurity.respository.UserDetailsRepository;
import tofu.code.useSecurity.entity.UserDetailsImpl;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    public UserDetails createNewUser(SignUpRequestDTO signupRequest){
        Set<SimpleGrantedAuthority> authorities = new H
        UserDetails newUser = UserDetailsImpl.builder()
                .username(signupRequest.getUsername()).password(signupRequest.getPassword())
                .enabled(true).accountNonLocked(true).accountNonExpired(true).credentialsNonExpired(true)
                .authorities().build();
        return userDetailsRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.findbyUsername(username);
    }
}
