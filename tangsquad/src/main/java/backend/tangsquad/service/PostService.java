package backend.tangsquad.service;

import backend.tangsquad.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    public PostListReadResponse getPosts(Integer page,
                                         Integer size,
                                         String sort)
    {
        return null;
    }

    public PostReadResponse getPost(Long id) {
        return null;
    }

    public PostCreateResponse createPost(PostCreateRequest request) {
        return null;
    }

    public void updatePost(Long id, PostUpdateRequest request) {
    }

    public void deletePost(Long id) {
    }
}
