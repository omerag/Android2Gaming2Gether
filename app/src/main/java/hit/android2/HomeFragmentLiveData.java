package hit.android2;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Database.Model.ParentData;
import hit.android2.Database.TopicDataHolder;

public class HomeFragmentLiveData extends ViewModel {

    private List<ParentData> topics;
    private List<TopicDataHolder> topicDataList;

    public HomeFragmentLiveData() {
    }

    public List<ParentData> getTopics() {
        return topics;
    }

    public void setTopics(List<ParentData> topics) {
        this.topics = topics;
    }

    public List<TopicDataHolder> getTopicDataHolderList() {
        return topicDataList;
    }

    public void setTopicDataHolderList(List<TopicDataHolder> topicDataList) {
        this.topicDataList = topicDataList;
    }

}
