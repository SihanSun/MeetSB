package cse110.com.meetsb.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UserSwipe {
    HashMap<String, String> liked;

    HashMap<String, String> matchList;

    public UserSwipe() {
        liked = new HashMap<>();
        matchList = new HashMap<>();
    }

    public HashMap<String, String> getLiked() { return liked; }

    public void setLiked(HashMap<String, String> liked) { this.liked = liked; }

    public HashMap<String, String> getMatchList() { return matchList; }

    public void setMatchList(HashMap<String, String> matchList) { this.matchList = matchList; }
}
