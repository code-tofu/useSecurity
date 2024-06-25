package tofu.code.useSecurity.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import tofu.code.useSecurity.entity.UserDetailsImpl;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsImpl, Long> {

     boolean existsByUsername(String username);

     UserDetailsImpl findByUsername(String username);

     UserDetailsImpl save(UserDetails newUser);


}
