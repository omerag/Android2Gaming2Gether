package hit.android2.Database;

public class CommentDataHolder {

    private String userName;
    private String massege;
    private String imageUrl;

    public CommentDataHolder() {
    }

    public CommentDataHolder(String userName, String massege, String imageUrl) {
        this.userName = userName;
        this.massege = massege;
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getMassege() {
        return massege;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMassege(String massege) {
        this.massege = massege;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
