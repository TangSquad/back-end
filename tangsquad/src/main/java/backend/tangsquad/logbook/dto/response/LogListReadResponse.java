package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.common.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LogListReadResponse {
    private List<LogReadResponse> Logs;
    private int currentPage;
    private int totalItems;
    private int totalPages;
}
