package hit.android2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.TopicAdapter;
import hit.android2.Database.ChildData;
import hit.android2.Database.ParentData;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        recyclerView = rootView.findViewById(R.id.home_recycler);

        List<ParentData> list = getList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TopicAdapter topicAdapter = new TopicAdapter(getActivity(),list);
        recyclerView.setAdapter(topicAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(topicAdapter);


        return rootView;
    }

    private List<ParentData> getList() {

        List<ParentData> list_parent =new ArrayList<>();
        List<ChildData> list_data_child = new ArrayList<>();

        list_data_child.add(new ChildData("First"));
        list_data_child.add(new ChildData("Second"));
        list_data_child.add(new ChildData("Third"));
        list_data_child.add(new ChildData("Four"));

        list_parent.add(new ParentData("Parent 1",list_data_child));
        list_parent.add(new ParentData("Parent 2",list_data_child));
        list_parent.add(new ParentData("Parent 3",list_data_child));
        list_parent.add(new ParentData("Parent 4",list_data_child));


        return list_parent;
    }
}
