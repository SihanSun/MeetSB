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

    private HashMap<String, HashSet<UserSwipe.WeightUser>> likesData;

    private HashMap<String, HashSet<UserSwipe.WeightUser>> swipeData;

    public List<String> getMatchedList() { return matchedList; }

    public void setMatchedList(List<String> matchedList) { this.matchedList = matchedList; }

    public HashMap<String, HashSet<UserSwipe.WeightUser>> getLikesData() { return likesData; }

    public void setLikesData(HashMap<String, HashSet<UserSwipe.WeightUser>> likesData) { this.likesData = likesData; }

    public HashMap<String, HashSet<UserSwipe.WeightUser>> getSwipeData() { return swipeData; }

    public void setSwipeData(HashMap<String, HashSet<UserSwipe.WeightUser>> swipeData) { this.swipeData = swipeData; }
}
