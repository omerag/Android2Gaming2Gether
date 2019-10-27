package hit.android2.Database.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserData implements Comparable<UserData>{

    private String key;

    private String name;
    private String aboutMe;
    private String gender;
    private Map<String,Boolean> language;
    private String country;
    private String imageUrl;

    private List<String> games = new ArrayList<>(); //gameId list
    private List<String> friends = new ArrayList<>();   //userId list
    private List<String> groups = new ArrayList<>(); //groupId list

    private int leader;
    private int teammate;
    private int sportsmanship;
    private int totalRank;

    private String birthday_timestamp;
    private double myLatitude;
    private     double myLongitude;

    public UserData() {
    }

    public UserData(String name,String key) {
        this.name = name;
        this.key = key;

        leader = 0;
        teammate = 0;
        sportsmanship = 0;
        totalRank = 0;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double myLongitude) {
        this.myLongitude = myLongitude;
    }

    public String getBirthday_timestamp() {
        return birthday_timestamp;
    }

    public void setBirthday_timestamp(String birthday_timestamp) {
        this.birthday_timestamp = birthday_timestamp;
    }

    public Map<String, Boolean> getLanguage() {
        return language;
    }

    public void setLanguage(Map<String, Boolean> language) {
        this.language = language;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public int compareTo(UserData userData) {
        if(totalRank >= userData.totalRank){
            return 1;
        }
        return  -1;

    }


}
