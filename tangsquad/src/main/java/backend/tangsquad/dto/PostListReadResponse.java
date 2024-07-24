package backend.tangsquad.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostListReadResponse {
    private List<PostReadResponse> posts;
    private int currentPage;
    private int totalItems;
    private int totalPages;
}