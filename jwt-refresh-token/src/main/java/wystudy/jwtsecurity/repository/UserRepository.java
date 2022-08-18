package wystudy.jwtsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wystudy.jwtsecurity.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User account);
    Optional<User> findById(String id);
}