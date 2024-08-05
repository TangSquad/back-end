package backend.tangsquad.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="diving")
public class Diving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // figma 작업 완료 후 개발

}
