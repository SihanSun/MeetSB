package cse110.com.meetsb.Model;
import android.media.Image;
import java.io.Serializable;

public class Message implements Serializable{
    private String id;
    private String userId;
    private String text;
    private String image_url;
    private Object time;
    private Boolean seen;
    private String nodeKey;

    public Message(String id, String userId, String text, String image_url, Object time, Boolean seen) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.image_url = image_url;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    @Override
    public String toString() {
        return " messages -> "+this.getText()+"  :-> imageurl "+this.getImage_url()+"\n";
    }
}
