package backend.tangsquad.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    // 유저 아이디
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 프로필 사진
    @Column private String profileImageUrl;

    // 소속
    @Column private String affiliation;

    // 상태 메세지
    @Column private String statusMessage;

    // 소개
    @Column private String introduction;

    // url
    @Column private String url;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Equipment equipment;


}


