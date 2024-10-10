package backend.tangsquad.moim.dto.response;

import backend.tangsquad.common.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MoimJoinResponse {

    private List<User> registeredUsers; // List of registered users

    @Builder
    public MoimJoinResponse(List<User> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

}
