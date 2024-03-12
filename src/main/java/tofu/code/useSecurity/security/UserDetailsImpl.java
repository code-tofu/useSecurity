package tofu.code.useSecurity.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "users")
public class UserDetailsImpl implements UserDetails {

    //interface fields
    @Column(length = 255)
    private Set<SimpleGrantedAuthority> authorities;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String username;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    //customFields
    @Id
    @GeneratedValue
    private Long userId;

}
