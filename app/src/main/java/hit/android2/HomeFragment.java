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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Adapters.TopicAdapter;
import hit.android2.Database.CommentDataHolder;
import hit.android2.Database.Model.ChildData;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.ParentData;
import hit.android2.Database.Model.UserData;
import hit.android2.Database.TopicDataHolder;

public class HomeFragment extends Fragment {

    private List<ParentData> topics;
    private List<TopicDataHolder> topicDataHolderList;
    private HomeFragmentLiveData liveData;
    private TopicAdapter topicAdapter;

    private RecyclerView recyclerView;
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

        topicAdapter = new TopicAdapter(getActivity(),topicDataHolderList,topics,liveData);
        recyclerView.setAdapter(topicAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(topicAdapter);
        //recyclerView.setHasFixedSize(true);


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
                        topicDataHolderList = initTopicDataHolderList(topics);
                        liveData.setTopicDataHolderList(topicDataHolderList);
                        Collections.sort(topicDataHolderList);
                        topicAdapter.setTopics(topicDataHolderList);
                        topicAdapter.notifyDataSetChanged();
                    }
                });

                Log.d("HomeFragment", "livedata set list");
            }
            else {
                topics = liveData.getTopics();
                topicDataHolderList = liveData.getTopicDataHolderList();
                Collections.sort(topicDataHolderList);
                topicAdapter.setTopics(topicDataHolderList);
                topicAdapter.notifyDataSetChanged();
            }
        }

    }

    private List<TopicDataHolder> initTopicDataHolderList(List<ParentData> topics){

        List<TopicDataHolder> topicDataHolderList = new ArrayList<>();


        for(ParentData topic : topics){

            List<CommentDataHolder> commentDataHolderList = new ArrayList<>();


            final TopicDataHolder dataHolder = new TopicDataHolder(topic.getTitle(),topic.getTimestamp(),commentDataHolderList,topic.getGame_key(),topic.getGame_key());
            topicDataHolderList.add(dataHolder);

            DatabaseManager.getGameFromDatabase(topic.getGame_key(), new DatabaseManager.DataListener<GameData>() {
                @Override
                public void onSuccess(GameData gameData) {

                    dataHolder.setGameName(gameData.getName());
                    dataHolder.setImageUrl(gameData.getImageUrl());
                    topicAdapter.notifyDataSetChanged();

                }
            });

            DatabaseManager.getUserFromDatabase(topic.getUser_key(), new DatabaseManager.DataListener<UserData>() {
                @Override
                public void onSuccess(UserData userData) {
                    dataHolder.setTopicsOwner(userData.getName());

                }
            });

            for(final ChildData comment : topic.getItems()){

                final CommentDataHolder commentDataHolder = new CommentDataHolder();
                commentDataHolderList.add(commentDataHolder);

                DatabaseManager.getUserFromDatabase(comment.getUser_key(), new DatabaseManager.DataListener<UserData>() {
                    @Override
                    public void onSuccess(UserData userData) {
                        commentDataHolder.setUserName(userData.getName());
                        commentDataHolder.setImageUrl(userData.getImageUrl());
                        commentDataHolder.setMassege(comment.getMassage());
                    }
                });
            }




        }
        return topicDataHolderList;
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
                final ParentData topic = new ParentData(topicEt.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),chosenGame.getGuid(),comments);
                DatabaseManager.addTopicToDatabase(chosenGame.getGuid(),topic);
                List<CommentDataHolder> commentDataHolderList = new ArrayList<>();
                final CommentDataHolder commentDataHolder = new CommentDataHolder();
                commentDataHolder.setMassege(massageEt.getText().toString());
                final TopicDataHolder topicDataHolder = new TopicDataHolder(topic.getTitle(),System.currentTimeMillis(),commentDataHolderList,FirebaseManager.getCurrentUserId(),chosenGame.getGuid());
                DatabaseManager.getUserFromDatabase(topic.getUser_key(), new DatabaseManager.DataListener<UserData>() {
                    @Override
                    public void onSuccess(UserData userData) {

                        topicDataHolder.setTitle(topic.getTitle());
                        topicDataHolder.setTopicsOwner(userData.getName());
                        topicDataHolder.setImageUrl(chosenGame.getImageUrl());
                        topicDataHolder.setGameName(chosenGame.getName());
                        topicDataHolder.setUserId(userData.getKey());
                        topicDataHolder.setGameId(chosenGame.getGuid());

                        commentDataHolder.setUserName(userData.getName());
                        commentDataHolder.setImageUrl(userData.getImageUrl());
                        topicAdapter.notifyDataSetChanged();

                    }
                });
                commentDataHolderList.add(commentDataHolder);
                topicDataHolderList.add(topicDataHolder);
                topicAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        dialog.show();
    }


}
