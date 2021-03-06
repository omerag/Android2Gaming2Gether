package hit.android2.Database.Model;

import java.util.List;

public class GroupData {

    private String key;
    private String group_name;
    private String image_URL;
    private List<String> members;

    public GroupData() {
    }

    public GroupData(String group_name, String image_URL, List<String> members) {
        this.group_name = group_name;
        this.image_URL = image_URL;
        this.members = members;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
