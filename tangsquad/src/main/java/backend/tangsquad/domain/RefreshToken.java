package backend.tangsquad.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Long id;

    @Column private String username;
    @Column private String token;
}
