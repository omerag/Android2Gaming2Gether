package hit.android2;

import java.util.List;

import hit.android2.Database.Model.UserData;

public class FriendsFragmentLiveData {

    List<UserData> friendsList;

    public List<UserData> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<UserData> friendsList) {
        this.friendsList = friendsList;
    }
}
