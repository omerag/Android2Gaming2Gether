package hit.android2.gaintbomb.game;

public class GameItem {

    private String gameId;
    private String name;
    private String imageUrl;

    public GameItem(String gameId) {
        this.gameId = gameId;
    }

    public GameItem(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public GameItem(String gameId, String name, String imageUrl) {
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
}
