package backend.tangsquad.diving.repository;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.moim.entity.Moim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivingRepository extends JpaRepository<Diving, Long> {
    List<Diving> findByUserId(Long userId);

    List<Diving> findByRegisteredUsersContaining(User user);

}
