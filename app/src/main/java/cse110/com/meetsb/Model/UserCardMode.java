package cse110.com.meetsb.Model;

import java.util.List;

import cse110.com.meetsb.Model.User;


public class UserCardMode {
    private String name;
    private int year;
    private List<String> images;

    public UserCardMode( User student ) {
        this.name = student.getPersonalInformation().getUserName();
        this.year = student.getAcademicInformation().getGraduationYear();
        //this.images = student.getPersonalInformation().getProfilePictures();
    }
    public UserCardMode(String name, int year, List<String> images) {
        this.name = name;
        this.year = year;
        this.images = images;
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
}
