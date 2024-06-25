package tofu.code.useSecurity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tofu.code.useSecurity.enums.Role;

import java.util.Collection;
import java.util.List;

import static tofu.code.useSecurity.enums.Authority.*;


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

    //Authorities implementation: Enum Based with Switch method
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.role) {
            case ADMIN -> List.of(
                new SimpleGrantedAuthority(String.valueOf(DELETE_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(UPDATE_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(WRITE_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(READ_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(PROTECTED_AUTHORITY))
            );
            case CREATOR -> List.of(
                new SimpleGrantedAuthority(String.valueOf(UPDATE_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(WRITE_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(READ_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(PROTECTED_AUTHORITY))
            );
            case VIEWER -> List.of(
                new SimpleGrantedAuthority(String.valueOf(READ_AUTHORITY)),
                new SimpleGrantedAuthority(String.valueOf(PROTECTED_AUTHORITY))
            );
            default -> List.of(new SimpleGrantedAuthority(String.valueOf(PROTECTED_AUTHORITY)));
        };



}
