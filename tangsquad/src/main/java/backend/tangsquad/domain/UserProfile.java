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
    private Long id;

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


    @PrePersist
    @PreUpdate
    private void setDefault() {
        if(this.profileImageUrl == null || this.profileImageUrl.isEmpty()) {
            this.profileImageUrl = "https://placehold.co/400";
        }
        if(this.affiliation == null || this.affiliation.isEmpty()) {
            this.affiliation = "";
        }
        if(this.statusMessage == null || this.statusMessage.isEmpty()) {
            this.statusMessage = "";
        }
        if(this.introduction == null || this.introduction.isEmpty()) {
            this.introduction = "";
        }
        if(this.url == null || this.url.isEmpty()) {
            this.url = "";
        }
    }

}


