package backend.tangsquad.moim.dto.request;

public class MoimLeaderUpdateByUsernameRequest {
    private Long moimId;
    private String newUsername;

    // Getters and Setters
    public Long getMoimId() {
        return moimId;
    }

    public void setMoimId(Long moimId) {
        this.moimId = moimId;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}
