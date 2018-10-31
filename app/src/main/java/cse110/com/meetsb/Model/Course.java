package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Course {

    private Map<String, List<String>> courseStudentMap;

    public Course() {
        courseStudentMap = new HashMap<>();
    }

    public void addCourse(String courseId) {
        if(!courseStudentMap.containsKey(courseId)) {
            courseStudentMap.put(courseId, new ArrayList<String>());
        }
    }

    public List<String> getStudentList(String courseId) {
        if(courseStudentMap.containsKey(courseId)) {
            return courseStudentMap.get(courseId);
        } else {
            return null;
        }
    }

    public void addStudent(String courseId, String userId) {
        if(!courseStudentMap.containsKey(courseId)) {
            courseStudentMap.put(courseId, new ArrayList<String>());
        }
        courseStudentMap.get(courseId).add(userId);
    }
}
