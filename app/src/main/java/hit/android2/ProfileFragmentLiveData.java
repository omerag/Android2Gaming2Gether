package hit.android2;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import hit.android2.Database.Model.GameData;

public class ProfileFragmentLiveData extends ViewModel {

    private String usernameTv;
    private String userIv;
    private String aboutMeTv;
    private List<GameData> gameDataList;

    public ProfileFragmentLiveData() {
        Log.d("LiveData","ProfileFragmentLiveData constructor called");

    }

    public String getUsernameTv() {
        return usernameTv;
    }

    public void setUsernameTv(String usernameTv) {
        this.usernameTv = usernameTv;
    }

    public String getAboutMeTv() {
        return aboutMeTv;
    }

    public void setAboutMeTv(String aboutMeTv) {
        this.aboutMeTv = aboutMeTv;
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
