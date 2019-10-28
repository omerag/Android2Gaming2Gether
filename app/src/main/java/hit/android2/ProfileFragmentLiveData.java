package hit.android2;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.Model.GameData;

public class ProfileFragmentLiveData extends ViewModel {

    private String usernameTv;
    private String userIv;
    private String aboutMeTv;
    private String birthday;
    private int age;
    private String level;
    private String gender;
    private List<GameData> gameDataList;
    private GameAdapter gameAdapter;


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

    public GameAdapter getGameAdapter() {
        return gameAdapter;
    }

    public void setGameAdapter(GameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
}
