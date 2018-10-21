package cse110.com.meetsb;

import java.util.List;


public class StudentCardMode {
    private String name;
    private int year;
    private List<String> images;

    public StudentCardMode( Student student ) {
        this.name = student.getName();
        this.year = student.getYear();
        this.images = student.getImages();
    }
    public StudentCardMode(String name, int year, List<String> images) {
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
