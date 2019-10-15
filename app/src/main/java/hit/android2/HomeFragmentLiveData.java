package hit.android2;

import androidx.lifecycle.ViewModel;

import java.util.List;

import hit.android2.Database.Model.ParentData;

public class HomeFragmentLiveData extends ViewModel {

    List<ParentData> topics;

    public HomeFragmentLiveData() {
    }

    public List<ParentData> getTopics() {
        return topics;
    }

    public void setTopics(List<ParentData> topics) {
        this.topics = topics;
    }
}
