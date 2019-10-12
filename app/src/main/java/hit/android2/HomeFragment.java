package hit.android2;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Adapters.TopicAdapter;
import hit.android2.Database.ChildData;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.FirebaseManager;
import hit.android2.Database.GameData;
import hit.android2.Database.ParentData;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    private GameData chosenGame = new GameData();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        FloatingActionButton addBtn = rootView.findViewById(R.id.home_fragment_add_btn);

        recyclerView = rootView.findViewById(R.id.home_recycler);

        List<ParentData> topics = new ArrayList<>(); //getList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TopicAdapter topicAdapter = new TopicAdapter(getActivity(),topics);
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
            DatabaseManager.getHomeTopics(FirebaseAuth.getInstance().getCurrentUser().getUid(),topics,topicAdapter);
        }

        return rootView;
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

        List<GameData> gameDataList = new ArrayList<>();
        GameAdapter gameAdapter = new GameAdapter(getContext(), gameDataList,"home", chosenGame);

        recyclerView.setAdapter(gameAdapter);
        recyclerView.setHasFixedSize(true);
        gameAdapter.notifyDataSetChanged();

        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(),gameDataList,gameAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ChildData> comments = new ArrayList<>();
                comments.add(new ChildData(FirebaseAuth.getInstance().getCurrentUser().getUid(),massageEt.getText().toString()));
                ParentData topic = new ParentData(topicEt.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),chosenGame.getGuid(),comments);
                DatabaseManager.addTopicToDatabase(chosenGame.getGuid(),topic);
            }
        });


        dialog.show();
    }






}
