package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.common.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogbookCreateRequest {

    @Column
    private LocalDateTime date;

    @Column(nullable = false)
    private String title;

    @Column
    private Long squadId;

    @Column
    private String contents;

    @Column
    private String location;

    @Column
    private Long weather;

    @Column
    private Float surfTemp;

    @Column
    private Float underTemp;

}
