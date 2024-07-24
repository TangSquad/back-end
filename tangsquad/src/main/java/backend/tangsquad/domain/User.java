package backend.tangsquad.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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

    // 가입 날짜
    @CreatedDate
    @Column(updatable = false) private LocalDateTime createdAt;

    // 자격증 리스트
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Certification> certifications;

    // 프로필
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    // 역할
    @Column(nullable = false) private String role;
}
