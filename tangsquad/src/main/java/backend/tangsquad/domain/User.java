package backend.tangsquad.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Integer id;

    // 이름
    @Column(nullable = false) private String name;

    // 닉네임
    @Column(nullable = false, unique = true) private String username;

    // 이메일
    @Column(nullable = false, unique = true) private String email;

    // 전화번호
    @Column(nullable = false, unique = true) private String phone;

    // 비밀번호(해시)
    @Column private String password;

    // 로그인 방법(일반, 카카오, 구글)
    @Column(nullable = false) private String loginMethod;

    // 가입 날짜
    @CreatedDate
    @Column(updatable = false) private LocalDateTime createdAt;
}
