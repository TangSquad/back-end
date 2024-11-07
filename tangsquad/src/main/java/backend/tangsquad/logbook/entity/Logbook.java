package backend.tangsquad.logbook.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
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
    private String thumbnailUrl;

    @Column
    private LocalDateTime date;

    @Column(nullable = false)
    private String title;

    @Column
    private String contents;

    @Column
    private String location;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "logs")
    private List<String> logs = new ArrayList<>();

    public void update(LogbookRequest logbookRequest) {
        if (logbookRequest.getDate() != null) this.date = logbookRequest.getDate();
        if (logbookRequest.getIsPublic() != null) this.isPublic = logbookRequest.getIsPublic();
        if (logbookRequest.getThumbnailUrl() != null) this.thumbnailUrl = logbookRequest.getThumbnailUrl();
        if (logbookRequest.getTitle() != null) this.title = logbookRequest.getTitle();
        if (logbookRequest.getContents() != null) this.contents = logbookRequest.getContents();
        if (logbookRequest.getLocation() != null) this.location = logbookRequest.getLocation();
    }

    @Builder
    public Logbook(User user, LocalDateTime date, Long currentPeople, String thumbnailUrl, String location, String title, String contents) {
        this.user = user;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.contents = contents;
        this.location = location;
    }
}
