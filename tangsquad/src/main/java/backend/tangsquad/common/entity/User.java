package backend.tangsquad.common.entity;

import backend.tangsquad.converter.LongListConverter;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.moim.entity.Moim;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Long id;

    // 이름
    @Column(nullable = false) private String name;

    // 닉네임
    @Column(nullable = false, unique = true) private String nickname;

    // 이메일
    @Column(nullable = false, unique = true) private String email;

    // 전화번호
    @Column(nullable = false, unique = true) private String phone;

    // 비밀번호
    @Column private String password;

    // 가입 플랫폼
    @Column private String platform;

    // 가입 날짜
    @CreatedDate
    @Column(updatable = false) private LocalDateTime createdAt;

    // 수정 날짜
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 프로필
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    // 역할
    @Column(nullable = false) private String role;

    // In User class
    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Logbook> logbooks;

    @ManyToMany
    @JoinTable(
            name = "user_likes_logbook",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "logbook_id")
    )
    private List<Logbook> likedLogbooks = new ArrayList<>(); // Initialize to avoid null issues



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonBackReference
    private List<Moim> moims;

}
