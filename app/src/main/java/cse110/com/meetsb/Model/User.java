package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cse110.com.meetsb.AcademicInfoActivity;

public class User {

    public static class PersonalInformation {
        private String profilePicture;

        private String Gender;

        private String description;

        private String userName;

        public String getUserName() { return userName; }

        public void setUserName(String userName) { this.userName = userName; }

        public String getProfilePicture() { return profilePicture; }

        public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

        public String getGender() { return Gender; }

        public void setGender(String gender) { Gender = gender; }

        public String getDescription() { return description; }

        public void setDescription(String description) { this.description = description; }
    }

    public static class AcademicInformation {
        private String gpa;

        private String major;

        private List<String> courseTaking;

        public String getMajor() { return major; }

        public void setMajor(String major) { this.major = major; }

        public String getGpa() { return gpa; }

        public void setGpa(String gpa) { this.gpa = gpa; }

        public List<String> getCourseTaking() { return courseTaking; }

        public void setCourseTaking(List<String> courseTaking) { this.courseTaking = courseTaking; }
    }

    public static class SwipeInformation {
        //should store user id
        private List<String> matchedList;

        private HashMap<String, HashSet<WeightUser>> likesData;

        private HashMap<String, HashSet<WeightUser>> swipeData;

        public List<String> getMatchedList() { return matchedList; }

        public void setMatchedList(List<String> matchedList) { this.matchedList = matchedList; }

        public HashMap<String, HashSet<WeightUser>> getLikesData() { return likesData; }

        public void setLikesData(HashMap<String, HashSet<WeightUser>> likesData) { this.likesData = likesData; }

        public HashMap<String, HashSet<WeightUser>> getSwipeData() { return swipeData; }

        public void setSwipeData(HashMap<String, HashSet<WeightUser>> swipeData) { this.swipeData = swipeData; }
    }

    class WeightUser {
        private String userId;

        int weight = 0;

        public String getUserId() { return userId; }

        public void setUserId(String userId) { this.userId = userId; }

        public int getWeight() { return weight; }

        public void setWeight(int weight) { this.weight = weight; }
    }

    public User() {
        PersonalInformation personalInformation = new PersonalInformation();
        this.personalInformation = personalInformation;

        AcademicInformation academicInformation = new AcademicInformation();
        academicInformation.courseTaking = new ArrayList<>();
        this.academicInformation = academicInformation;

        SwipeInformation swipeInformation = new SwipeInformation();
        swipeInformation.likesData = new HashMap<>();
        swipeInformation.matchedList = new ArrayList<>();
        swipeInformation.swipeData = new HashMap<>();
        this.setSwipeInformation(swipeInformation);
    }


    private PersonalInformation personalInformation;

    private AcademicInformation academicInformation;

    private SwipeInformation swipeInformation;


    public PersonalInformation getPersonalInformation() { return this.personalInformation; }

    public void setPersonalInformation(PersonalInformation personalInformation) { this.personalInformation = personalInformation; }

    public AcademicInformation getAcademicInformation() { return this.academicInformation; }

    public void setAcademicInformation(AcademicInformation academicInformation) { this.academicInformation = academicInformation; }

    public SwipeInformation getSwipeInformation() { return this.swipeInformation; }

    public void setSwipeInformation(SwipeInformation swipeInformation) { this.swipeInformation = swipeInformation; }
}

