package hit.android2.Database;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TopicDataHolder implements Comparable<TopicDataHolder>{

    private String title;
    private String gameName;
    private String topicsOwner;
    private String date;
    private String imageUrl;
    private List<CommentDataHolder> comments;

    private String userId;
    private String gameId;
    private String topicId;

    public TopicDataHolder() {
    }

    public TopicDataHolder(String title, long timestemp,List<CommentDataHolder> comments, String userId, String gameId, String topicId) {
        this.title = title;
        this.comments = comments;
        this.userId = userId;
        this.gameId = gameId;
        this.topicId = topicId;

        date = new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date(timestemp));

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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    @Override
    public int compareTo(TopicDataHolder topicDataHolder) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date date1 = format.parse(topicDataHolder.getDate(), new ParsePosition(0));
        Date date2 = format.parse(getDate(), new ParsePosition(0));

        if(date2.getTime() - date1.getTime()   >= 0){
            return -1;
        }
        return 1;
    }
}
