package cse110.com.meetsb;

public class Course {
    private int year;
    private int month;
    private String grade;

    public Course() {
        this(0,0,"");
    }

    public Course(int year, int month, String grade) {
        this.year = year;
        this.month = month;
        this.grade = grade;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        String pattern = "";
        if (grade.equals("A") || grade.equals("A+")
                || grade.equals("B"))
            this.grade = grade;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
