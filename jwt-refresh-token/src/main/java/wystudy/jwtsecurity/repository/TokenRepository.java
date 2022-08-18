package wystudy.jwtsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wystudy.jwtsecurity.entity.RefreshToken;

import javax.transaction.Transactional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByRefreshToken(String token);

    @Transactional
    void deleteByRefreshToken(String token);
}
