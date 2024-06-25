package tofu.code.useSecurity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetailsImpl implements UserDetails {

    //interface fields
    @Column(length = 255)
    private String authorities;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //V1 authorities implementation: Role Based Single String
    @Override
    public List<SimpleGrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(this.authorities));
    }

}
