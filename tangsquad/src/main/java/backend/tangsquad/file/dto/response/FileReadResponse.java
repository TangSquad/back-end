package backend.tangsquad.file.dto.response;

public class FileReadResponse {
    private String url;

    public FileReadResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
