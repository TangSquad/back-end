package backend.tangsquad.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Integer id;

    // 유저 엔티티와 일대일 관계, 유저 엔티티가 삭제되면 같이 삭제
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // 비밀번호(해시)
    @Column private String password;

    // 솔트
    @Column private String salt;
}
