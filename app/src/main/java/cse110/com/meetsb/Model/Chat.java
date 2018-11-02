package cse110.com.meetsb.Model;

public class Chat {
    private String id;
    private User user;
    private String message;
    private String timeStamp;

    public Chat(){}

    public Chat(User user, String message, String timeStamp){
        this.user = user;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "user=" + user +
                ", timeStamp='" + timeStamp + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
