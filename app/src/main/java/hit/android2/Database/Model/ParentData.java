package hit.android2.Database.Model;


import java.util.List;

public class ParentData implements Comparable<ParentData> {

    private String title;
    private long timestamp;
    private String id;
    private String user_key;
    private String game_key;
    private List<ChildData> items;

    public ParentData(){
    }

/*
    public ParentData(String title, List<ChildData> items){
        this.title = title;
        this.items = items;

    }
*/

    public ParentData(String title, String user_key, String game_key, List<ChildData> items) {
        this.title = title;
        this.user_key = user_key;
        this.game_key = game_key;
        this.items = items;
        timestamp = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public List<ChildData> getItems() {
        return items;
    }

    public void setItems(List<ChildData> items) {
        this.items = items;
    }

    public String getGame_key() {
        return game_key;
    }

    public void setGame_key(String game_key) {
        this.game_key = game_key;
    }

    @Override
    public int compareTo(ParentData parentData) {
        if(timestamp - parentData.timestamp >= 0){
            return -1;
        }
        return  1;
    }
}
