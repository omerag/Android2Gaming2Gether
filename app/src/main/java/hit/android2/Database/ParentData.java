package hit.android2.Database;


import java.util.List;

public class ParentData  {

    private String title;
    private String time;
    private String id;
    private String user_key;

    public ParentData(){
    }

    public ParentData(String title, List<ChildData> items){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
