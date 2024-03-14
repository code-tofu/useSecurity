package tofu.code.useSecurity.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

     boolean existsByUsername(String username);

     UserDetails findbyUsername(String username);

     UserDetails save(UserDetails newUser);


}
