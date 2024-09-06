package backend.tangsquad.common.repository;

import backend.tangsquad.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhone(String phone);
}
