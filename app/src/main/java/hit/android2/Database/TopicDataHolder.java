package hit.android2.Database;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class TopicDataHolder {

    private String title;
    private String gameName;
    private String topicsOwner;
    private String date;
    private String imageUrl;
    private List<CommentDataHolder> comments;

    private String userId;
    private String gameId;


    public TopicDataHolder() {
    }

    public TopicDataHolder(String title, long timestemp,List<CommentDataHolder> comments, String userId, String gameId) {
        this.title = title;
        date = new Date(timestemp).toString();
        this.comments = comments;
        this.userId = userId;
        this.gameId = gameId;
    }

    public TopicDataHolder(String title, String gameName, String topicsOwner, String date, String imageUrl) {
        this.title = title;
        this.gameName = gameName;
        this.topicsOwner = topicsOwner;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getGameName() {
        return gameName;
    }

    public String getTopicsOwner() {
        return topicsOwner;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<CommentDataHolder> getComments() {
        return comments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setTopicsOwner(String topicsOwner) {
        this.topicsOwner = topicsOwner;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setComments(List<CommentDataHolder> comments) {
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
