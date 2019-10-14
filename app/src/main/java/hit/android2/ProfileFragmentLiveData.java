package hit.android2;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.List;

import hit.android2.Database.Model.GameData;

public class ProfileFragmentLiveData extends ViewModel {

    String usernameTv;
    String userIv;
    List<GameData> gameDataList;

    public ProfileFragmentLiveData() {
        Log.d("LiveData","ProfileFragmentLiveData constructor called");
    }

    public String getUsernameTv() {
        return usernameTv;
    }

    public void setUsernameTv(String usernameTv) {
        this.usernameTv = usernameTv;
    }

    public String getUserIv() {
        return userIv;
    }

    public void setUserIv(String userIv) {
        this.userIv = userIv;
    }

    public List<GameData> getGameDataList() {
        return gameDataList;
    }

    public void setGameDataList(List<GameData> gameDataList) {
        this.gameDataList = gameDataList;
    }
}
