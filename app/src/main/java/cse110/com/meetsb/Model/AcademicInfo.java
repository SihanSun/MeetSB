package cse110.com.meetsb.Model;

import java.util.*;

public class AcademicInfo {
    private double gpa = 0.0;
    private List<Course> classList;
    private List<Course> previousList;

    public AcademicInfo(){
        classList = new ArrayList();
        previousList = new ArrayList();
    }

}
