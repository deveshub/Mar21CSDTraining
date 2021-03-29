package domain;

public class UserInfo {

    private String userName;
    private long userId;
    private String role;
    private boolean inActive;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getUserId() {
        return this.userId;
    }

    public String getRole() {
        return this.role;
    }

    public void setInActive(boolean active) {
        this.inActive = active;
    }

    public boolean isInActive() {
        return inActive;
    }
}
