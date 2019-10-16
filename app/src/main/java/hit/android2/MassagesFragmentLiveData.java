package hit.android2;

import androidx.lifecycle.ViewModel;

import java.util.List;

import hit.android2.Database.Model.UserData;
import hit.android2.Model.Chatlist;

public class MassagesFragmentLiveData extends ViewModel {

    private List<UserData> mUsers;
    private List<Chatlist> usersList;


    public MassagesFragmentLiveData() {
    }

    public List<UserData> getmUsers() {
        return mUsers;
    }

    public void setmUsers(List<UserData> mUsers) {
        this.mUsers = mUsers;
    }

    public List<Chatlist> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Chatlist> usersList) {
        this.usersList = usersList;
    }
}
