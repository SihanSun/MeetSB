package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UserSwipe {

    class WeightUser {
        private String userId;

        int weight = 0;

        public String getUserId() { return userId; }

        public void setUserId(String userId) { this.userId = userId; }

        public int getWeight() { return weight; }

        public void setWeight(int weight) { this.weight = weight; }
    }

    public UserSwipe() {
        this.likesData = new HashMap<>();
        this.matchedList = new ArrayList<>();
        this.swipeData = new HashMap<>();
    }

    //should store user id
    private List<String> matchedList;

    private HashMap<String, HashMap<String, Integer>> likesData;

    private HashMap<String, HashMap<String, Integer>> swipeData;

    public List<String> getMatchedList() { return matchedList; }

    public void setMatchedList(List<String> matchedList) { this.matchedList = matchedList; }

    public HashMap<String, HashMap<String, Integer>> getLikesData() { return likesData; }

    public void setLikesData(HashMap<String, HashMap<String, Integer>> likesData) { this.likesData = likesData; }

    public HashMap<String, HashMap<String, Integer>> getSwipeData() { return swipeData; }

    public void setSwipeData(HashMap<String, HashMap<String, Integer>> swipeData) { this.swipeData = swipeData; }
}
