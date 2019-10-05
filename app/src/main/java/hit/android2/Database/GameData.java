package hit.android2.Database;

import java.util.ArrayList;
import java.util.List;

public class GameData {

    private String guid;
    private String name;
    private String imageUrl;

    private List<String> usersList = new ArrayList<>();

    public GameData() {
    }

    public GameData(String guid) {
        this.guid = guid;
    }

    public GameData(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public GameData(String guid, String name, String imageUrl) {
        this.guid = guid;
        this.name = name;
        this.imageUrl = imageUrl;

    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }
}
