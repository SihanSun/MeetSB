package cse110.com.meetsb.Model;
import java.util.*;

public class User {
        private String userId;
        private String userName;
        private String userPassword;
        private PersonalInfo personalInfo;
        private AcademicInfo academicInfo;
        private List<User> matched;
        private HashMap<String, Integer> hashMap;

        public User(){
            userId = null;
            userName = null;
            userPassword = null;
            personalInfo = null;
            academicInfo = null;
            matched = new ArrayList<>();
            hashMap = new HashMap<>();
        }

        public void setuserId(String userId){
            this.userId = userId;
        }

        public String getuserId(){
            return this.userId;
        }

        public void setuserName(String userName){
            this.userName = userName;
        }

        public String getuserName(){
            return this.userName;
        }

        public void setuserPassword(String userPassword){
            this.userPassword = userPassword;
        }

        public String getUserPassword(){
            return this.userPassword;
        }

        public void setacademicInfo(AcademicInfo academicInfo){
            this.academicInfo = academicInfo;
        }

        public AcademicInfo getAcademicInfo(){
            return this.academicInfo;
        }
        public HashMap<String, Integer> getHashMap() {
            return hashMap;
        }
        public void setHashMap(HashMap<String, Integer> hashMap) {
            this.hashMap = hashMap;
        }
}
