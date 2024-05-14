package tofu.code.useSecurity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserDetailsImpl implements UserDetails {

    //interface fields
    @Column(length = 255)
    private Set<SimpleGrantedAuthority> authorities; //role based, single string even though Set

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
