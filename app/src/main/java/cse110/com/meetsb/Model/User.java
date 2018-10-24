package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Pair;

public class User {

    private String userId;

    private String userName;

    private String userPassword;

    private String userPictureURL;

    private String gender;

    private String description;

    private double gpa;

    private ArrayList<String>  courseList;

    private ArrayList<String>  courseTakenBefore;

    private ArrayList<String>  matchPeople;

    private Map<String, Set<String>> likePeople;

    private Map<String, Set<Pair<String, int>>>  swipPeople;

    public User() {
        this.userId = null;
        this.userName = null;
        this.userPassword = null;
        this.userPictureURL = null;
        this.gender = null;
        this.description = null;
        this.gpa = 0.0;
        this.courseList = new ArrayList<String>();
        this.courseTakenBefore = new ArrayList<String>();
        this.matchPeople = new ArrayList<String>();
        this.likePeople = new Map<String, Set<String>>();
        this.swipPeople = new Map<String, Set<Pair<String, int>>>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPictureURL() {
        return userPictureURL;
    }

    public void setUserPictureURL(String userPictureURL) {
        this.userPictureURL = userPictureURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public ArrayList<String> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<String> courseList) {
        this.courseList = courseList;
    }

    public bool addTakingCourse(String courseId) {
        return courseList.add(courseId);
    }

    public ArrayList<String> getCourseTakenBefore() {
        return courseTakenBefore;
    }

    public void setCourseTakenBefore(ArrayList<String> courseTakenBefore) {
        this.courseTakenBefore = courseTakenBefore;
    }

    public ArrayList<String> getMatchPeople() {
        return matchPeople;
    }

    public void setMatchPeople(ArrayList<String> matchPeople) {
        this.matchPeople = matchPeople;
    }

    public bool addMatchPeople(String userId) {
        return matchPeople.add(userId);
    }

    public Map<String, Set<String>> getLikePeople() {
        return likePeople;
    }

    public void setLikePeople(Map<String, Set<String>> likePeople) {
        this.likePeople = likePeople;
    }

    public bool addLikePeople(String courseId, String userId) {
        return (likePeople.get(courseId)).add(userId);
    }

    public Map<String, Set<Pair<String, int>>> getSwipPeople() {
        return swipPeople;
    }

    public void setSwipPeople(Map<String, Set<Pair<String, int>>> swipPeople) {
        this.swipPeople = swipPeople;
    }
}
