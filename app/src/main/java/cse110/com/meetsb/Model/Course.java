package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Course {

    private Map<String, Integer> studentOffsetMap;

    private List<String> studentsInTheCourse;

    public Course() {
        studentOffsetMap = new HashMap<>();
        studentsInTheCourse = new ArrayList<>();
    }

    public Map<String, Integer> getStudentOffsetMap() { return studentOffsetMap; }

    public void setStudentOffsetMap(Map<String, Integer> studentOffsetMap) { this.studentOffsetMap = studentOffsetMap; }

    public void setStudentsInTheCourse(List<String> studentsInTheCourse) { this.studentsInTheCourse = studentsInTheCourse; }

    public List<String> getStudentsInTheCourse() {
        return this.studentsInTheCourse;
    }

    public List<String> getStudentList(String UID, int size) {
        if(size <= 0) {
            return null;
        }

        //get the current offset
        if(!studentOffsetMap.containsKey(UID) ) {
            //if the student has not in the list before, give offset 0 to the student
            //and add the student to the list of students taking this course.
            studentOffsetMap.put(UID, 0);
            studentsInTheCourse.add(UID);
        }
        int offset = studentOffsetMap.get(UID);

        //check whether the user has already consumed all the list
        int studentSize = studentsInTheCourse.size();
        if(offset == studentSize) {
            return null;
        }

        //read students in the list
        List<String> result = new ArrayList<>();
        int count = 0;
        while(count < size) {
            if(offset < studentSize) {
                result.add(studentsInTheCourse.get(offset));
                offset++;
                count++;
            } else {
                break;
            }
        }

        //update the map
        studentOffsetMap.put(UID, offset);

        //return the list
        return result;
    }
}
