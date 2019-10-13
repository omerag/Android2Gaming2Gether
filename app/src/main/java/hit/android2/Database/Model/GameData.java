package hit.android2.Database.Model;

import java.util.ArrayList;
import java.util.List;

public class GameData {

    private String guid = null;
    private String name = null;
    private String imageUrl = null;

    private List<String> users = new ArrayList<>();

    public GameData() {
    }


    public GameData(String guid, String name, String imageUrl) {
        this.guid = guid;
        this.name = name;
        this.imageUrl = imageUrl;

    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
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

    public List<String> getUsers() {
        return users;
    }

}
