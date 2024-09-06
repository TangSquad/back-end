package backend.tangsquad.repository;

import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MoimRepository extends JpaRepository<Moim, Long> {
    List<Moim> findByUserId(Long userId);
    List<Moim> findByRegisteredUsersContaining(User user);

}
