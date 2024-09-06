package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.domain.User;
import backend.tangsquad.logbook.dto.response.LogReadResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LogListReadResponse {
    private User user;
    private List<LogReadResponse> Logs;
    private int currentPage;
    private int totalItems;
    private int totalPages;
}