package backend.tangsquad.moim.dto.response;

import backend.tangsquad.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class MoimJoinResponse {
    public List<User> registeredUsers;
}
