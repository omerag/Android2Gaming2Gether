package hit.android2.Database;

import java.util.List;

public class GameData {

    private String gameId;
    private String name;
    private String imageUrl;

    private List<String> usersList;

    public GameData() {
    }

    public GameData(String gameId) {
        this.gameId = gameId;
    }

    public GameData(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public GameData(String gameId, String name, String imageUrl) {
        this.gameId = gameId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
