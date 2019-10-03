package hit.android2.Database;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    private String username;
    private String aboutMe;

    private String avatarUrl;

    private List<String> games = new ArrayList<>(); //gameId list
    private List<String> friends = new ArrayList<>();   //userId list

    private int leader;
    private int teammate;
    private int sportsmanship;
    private int totalRank;

    public UserData() {
    }

    public UserData(String username) {
        this.username = username;

        leader = 0;
        teammate = 0;
        sportsmanship = 0;
        totalRank = 0;


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<String> getGames() {
        return games;
    }

    public void setGames(List<String> games) {
        this.games = games;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }

    public int getTeammate() {
        return teammate;
    }

    public void setTeammate(int teammate) {
        this.teammate = teammate;
    }

    public int getSportsmanship() {
        return sportsmanship;
    }

    public void setSportsmanship(int sportsmanship) {
        this.sportsmanship = sportsmanship;
    }

    public int getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(int totalRank) {
        this.totalRank = totalRank;
    }

    public void updateTotalRank(){
        totalRank = (leader + teammate + sportsmanship) / 3;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}