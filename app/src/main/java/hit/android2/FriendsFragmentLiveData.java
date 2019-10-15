package hit.android2;

import androidx.lifecycle.ViewModel;

import java.util.List;

import hit.android2.Database.Model.UserData;

public class FriendsFragmentLiveData extends ViewModel {

    List<UserData> friendsList;

    public FriendsFragmentLiveData() {
    }

    public List<UserData> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<UserData> friendsList) {
        this.friendsList = friendsList;
    }
}
