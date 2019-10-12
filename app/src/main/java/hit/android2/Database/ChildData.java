package hit.android2.Database;


public class ChildData{

    private String id;
    private String massage;
    private String time;
    private String user_key;

    public ChildData() {
    }

    public ChildData(String user_key,String massage) {
        this.user_key = user_key;
        this.massage = massage;

    }

    public ChildData(String massage, String time, String user_key) {
        this.massage = massage;
        this.time = time;
        this.user_key = user_key;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }


    public String getTime() {
        return time;
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
