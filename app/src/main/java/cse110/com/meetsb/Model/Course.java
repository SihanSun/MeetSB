package cse110.com.meetsb.Model;

import java.util.*;

public class Course {
    private String courseId;
    private List<User> studentList;

    public Course(){
        studentList = new ArrayList();
    }
    public void addStudent(User student){
        studentList.add(student);
    }
}