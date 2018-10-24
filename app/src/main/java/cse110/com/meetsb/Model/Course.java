package cse110.com.meetsb.Model;

import java.util.Set;

public class Course {

    private String courseId;

    private Set<String>  studentList;

    public Course() {
        this.courseId = null;
        this.studentList = new Set<String>();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Set<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(Set<String> studentList) {
        this.studentList = studentList;
    }

    public bool addStudentToCourse(String userId) {
        return studentList.add(userId);
    }
}
