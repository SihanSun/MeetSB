package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UserSwipe {
    HashMap<String, Integer> liked;

    List<String> matchList;

    public UserSwipe() {
        liked = new HashMap<>();
        matchList = new ArrayList<>();
    }

    public HashMap<String, Integer> getLiked() { return liked; }

    public void setLiked(HashMap<String, Integer> liked) { this.liked = liked; }

    public List<String> getMatchList() { return matchList; }

    public void setMatchList(List<String> matchList) { this.matchList = matchList; }
}
