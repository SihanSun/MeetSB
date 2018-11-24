package cse110.com.meetsb.Model;

import java.util.List;


public class UserCardMode {
    private String name;
    private int year;
    private String uid;
    private List<String> images;

    public UserCardMode( User student ) {
        this.name = student.getUserName();
        this.year = student.getGraduationYear();
        this.images = student.getProfilePictures();
    }
    public UserCardMode(String name, int year, List<String> images, String uid) {
        this.name = name;
        this.year = year;
        this.images = images;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }
}
