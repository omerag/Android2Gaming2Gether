package hit.android2;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Adapters.TopicAdapter;
import hit.android2.Database.Model.ChildData;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.FirebaseManager;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.ParentData;

public class HomeFragment extends Fragment {

    List<ParentData> topics;
    HomeFragmentLiveData liveData;

    RecyclerView recyclerView;
    private GameData chosenGame = new GameData();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        liveData = ViewModelProviders.of(this).get(HomeFragmentLiveData.class);

        FloatingActionButton addBtn = getActivity().findViewById(R.id.home_fragment_add_btn);

        recyclerView = getActivity().findViewById(R.id.home_recycler);

        topics = new ArrayList<>(); //getList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final TopicAdapter topicAdapter = new TopicAdapter(getActivity(),topics);
        recyclerView.setAdapter(topicAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(topicAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateTopicDialog();
            }
        });

        if(FirebaseManager.isLoged()){
            if(liveData.getTopics() == null){
                DatabaseManager.getHomeTopics(FirebaseAuth.getInstance().getCurrentUser().getUid(), topics, new DatabaseManager.Listener() {
                    @Override
                    public void onSuccess() {
                        liveData.setTopics(topics);
                        topicAdapter.notifyDataSetChanged();
                    }
                });

                Log.d("HomeFragment", "livedata set list");
            }
            else {
                topics = liveData.getTopics();
                topicAdapter.setTopics(topics);
                topicAdapter.notifyDataSetChanged();
            }
        }

    }

    private void showCreateTopicDialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_create_new_topic);

        dialog.setTitle("New Topic Dialog");

        final EditText topicEt = dialog.findViewById(R.id.create_new_topic_dialog_topic_name_edit_text);
        final EditText massageEt = dialog.findViewById(R.id.create_new_topic_dialog_massage_edit_text);
        ImageButton sendBtn = dialog.findViewById(R.id.create_new_topic_dialog_imageBtn);
        recyclerView = dialog.findViewById(R.id.create_new_topic_dialog_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final List<GameData> gameDataList = new ArrayList<>();
        GameAdapter gameAdapter = new GameAdapter(getContext(), gameDataList, chosenGame);
        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                GameData tempGame = gameDataList.get(position);
                chosenGame.setName(tempGame.getName());
                chosenGame.setImageUrl(tempGame.getImageUrl());
                chosenGame.setGuid(tempGame.getGuid());

                Toast.makeText(getActivity(), chosenGame.getName() + " picked", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(gameAdapter);
        recyclerView.setHasFixedSize(true);
        gameAdapter.notifyDataSetChanged();

        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(),gameDataList,gameAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ChildData> comments = new ArrayList<>();
                comments.add(new ChildData(massageEt.getText().toString(),System.currentTimeMillis(),FirebaseAuth.getInstance().getCurrentUser().getUid()));
                ParentData topic = new ParentData(topicEt.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),chosenGame.getGuid(),comments);
                DatabaseManager.addTopicToDatabase(chosenGame.getGuid(),topic);
            }
        });


        dialog.show();
    }






}
