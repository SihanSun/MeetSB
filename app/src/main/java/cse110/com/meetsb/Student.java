package cse110.com.meetsb;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private int year;
    private double gpa;
    private int age;
    private List<String> images;
    private List<Course> classes;

    public Student() {
        images = new ArrayList<>();
        classes = new  ArrayList<>();
    }
    public Student(String name, int year, List<String> images) {
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
