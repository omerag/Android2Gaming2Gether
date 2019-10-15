package hit.android2;

import androidx.lifecycle.ViewModel;

import java.util.List;

import hit.android2.Database.Model.UserData;

public class MassagesFragmentLiveData extends ViewModel {

    private List<UserData> mUsers;
    private List<String> usersList;


    public MassagesFragmentLiveData() {
    }

    public List<UserData> getmUsers() {
        return mUsers;
    }

    public void setmUsers(List<UserData> mUsers) {
        this.mUsers = mUsers;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }
}
