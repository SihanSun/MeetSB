package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class User {

    class PersonalInformation {
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

    class AcademicInformation {
        private double gpa;

        private List<String> courseTaking;

        public double getGpa() { return gpa; }

        public void setGpa(double gpa) { this.gpa = gpa; }

        public List<String> getCourseTaking() { return courseTaking; }

        public void setCourseTaking(List<String> courseTaking) { this.courseTaking = courseTaking; }
    }

    class SwipeInformation {
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

    static class UserBuilder {
        private String userId;

        private String userName = "Undecided";

        private String userPassword = "12345678";

        private String gender = "Undecided";

        private String description = "N/A";

        private String profilePicture = null;

        private double gpa = 0.0;

        private List<String> courseTaking = new ArrayList<>();

        private List<String> matchedList = new ArrayList<>();

        private HashMap<String, HashSet<WeightUser>> likesData = new HashMap<>();

        private HashMap<String, HashSet<WeightUser>> swipeData = new HashMap<>();

        public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

        public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

        public void setUserId(String userId) { this. userId = userId; };

        public void setGender(String gender) { this.gender = gender; };

        public void setGpa(double gpa) { this.gpa = gpa; }

        public void setUserName(String userName) { this.userName = userName; }

        public void setDescription(String description) { this.description = description; }

        public void addCoursesTaken(String course) {
            if(courseTaking == null) {
                courseTaking = new ArrayList<>();
            }
            courseTaking.add(course);
        }

        public User build() {
            return new User(this);
        }
    }

    private User(UserBuilder userBuilder) {

        this.userId = userBuilder.userId;

        this.userPassword = userBuilder.userPassword;

        //personal information
        PersonalInformation personalInformation = new PersonalInformation();
        personalInformation.setDescription(userBuilder.description);
        personalInformation.setGender(userBuilder.gender);
        personalInformation.setProfilePicture(userBuilder.profilePicture);
        personalInformation.setUserName(userBuilder.userName);
        this.personalInformation = personalInformation;

        //academic information
        AcademicInformation academicInformation = new AcademicInformation();
        academicInformation.setCourseTaking(userBuilder.courseTaking);
        academicInformation.setGpa(userBuilder.gpa);
        this.academicInformation = academicInformation;

        //swipe information
        SwipeInformation swipeInformation = new SwipeInformation();
        swipeInformation.setLikesData(userBuilder.likesData);
        swipeInformation.setMatchedList(userBuilder.matchedList);
        swipeInformation.setLikesData(userBuilder.likesData);
        swipeInformation.setSwipeData(userBuilder.swipeData);
        this.swipeInformation = swipeInformation;
    }

    private String userId;

    private String userPassword;

    private PersonalInformation personalInformation;

    private AcademicInformation academicInformation;

    private SwipeInformation swipeInformation;

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getUserPassword() { return userPassword; }

    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public PersonalInformation getPersonalInformation() { return personalInformation; }

    public void setPersonalInformation(PersonalInformation personalInformation) { this.personalInformation = personalInformation; }

    public AcademicInformation getAcademicInformation() { return academicInformation; }

    public void setAcademicInformation(AcademicInformation academicInformation) { this.academicInformation = academicInformation; }

    public SwipeInformation getSwipeInformation() { return swipeInformation; }

    public void setSwipeInformation(SwipeInformation swipeInformation) { this.swipeInformation = swipeInformation; }
}

