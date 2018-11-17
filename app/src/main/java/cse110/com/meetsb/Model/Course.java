package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Course {

    private List<String> studentsInTheCourse;

    public Course() {
        studentsInTheCourse = new ArrayList<>();
    }

    public void setStudentsInTheCourse(List<String> studentsInTheCourse) { this.studentsInTheCourse = studentsInTheCourse; }

    public List<String> getStudentsInTheCourse() {
        return this.studentsInTheCourse;
    }
}
