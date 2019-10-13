package hit.android2.Database.Model;


public class ChildData{

    private String id;
    private String massage;
    private long timestamp;
    private String user_key;

    public ChildData() {
    }


    public ChildData(String massage, long timestamp, String user_key) {
        this.massage = massage;
        this.timestamp = timestamp;
        this.user_key = user_key;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }


    public long getTimestamp() {
        return timestamp;
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
}
