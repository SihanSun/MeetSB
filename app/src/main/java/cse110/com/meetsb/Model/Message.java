package cse110.com.meetsb.Model;
import android.media.Image;
import android.net.Uri;

import java.io.Serializable;

public class Message implements Serializable{
    private String id;
    private String userId;
    private String text;
    private Object time;
    private Boolean seen;
    public Uri imageUri;

    public Message(){}

    public Message(String id, String userId, Uri imageUri, String text, Object time, Boolean seen) {
        this.id = id;
        this.userId = userId;
        this.imageUri = imageUri;
        this.text = text;
        this.time = time;
        this.seen = seen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return " messages -> "+this.getText()+"\n";
    }
}
