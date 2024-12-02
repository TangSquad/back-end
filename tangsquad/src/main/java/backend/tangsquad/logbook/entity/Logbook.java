package backend.tangsquad.logbook.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.dto.request.ThumbnailRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "logbook")
@NoArgsConstructor
public class Logbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @Column
    private Boolean isPublic;

    @Column
    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

    @Column
    private String thumbnailUrl;

    @Column
    private String date;

    @Column(nullable = false)
    private String title;

    @Column
    private String contents;

    @OneToMany(mappedBy = "logbook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Log> logs = new ArrayList<>();

    // --- 컨디션
    @Column
    @Enumerated(EnumType.STRING)
    private UserCondition userCondition;

    public void update(LogbookRequest logbookRequest) {
        if (logbookRequest.getDate() != null) this.date = logbookRequest.getDate();
        if (logbookRequest.getIsPublic() != null) this.isPublic = logbookRequest.getIsPublic();
        if (logbookRequest.getImageUrls() != null) this.imageUrls = logbookRequest.getImageUrls();
        if (logbookRequest.getTitle() != null) this.title = logbookRequest.getTitle();
        if (logbookRequest.getContents() != null) this.contents = logbookRequest.getContents();
        if (logbookRequest.getUserCondition() != null) this.userCondition = logbookRequest.getUserCondition();

        if (logbookRequest.getImageUrls().size() != 0) {
            this.thumbnailUrl = this.imageUrls.get(0);
        }
    }

    public void updateThumbnail(ThumbnailRequest thumbnailRequest) {
        this.thumbnailUrl = thumbnailRequest.getThumbnailUrl();
    }

    public synchronized void addLog(Log addedLog) {
        if (addedLog == null) {
            throw new IllegalArgumentException("Log cannot be null");
        }
        if (logs == null) {
            System.out.println("logs are null");
            logs = new ArrayList<>();
        }
        System.out.println("Adding log: " + logs);
        this.logs.add(addedLog);
    }


    @Builder
    public Logbook(User user, Boolean isPublic, String date, List<String> imageUrls, String title, String contents, List<Log> logs, UserCondition userCondition) {
        this.user = user;
        this.isPublic = isPublic;
        this.date = date;
        this.imageUrls = imageUrls;
        this.title = title;
        this.contents = contents;
        this.logs = logs;
        this.userCondition = userCondition;
    }

}
