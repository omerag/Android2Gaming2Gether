package hit.android2;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    private List<ParentData> dbTopics;
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
        topicDataHolderList = new ArrayList<>();
        dbTopics = new ArrayList<>(); //getList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        topicAdapter = new TopicAdapter(getActivity(), topicDataHolderList, dbTopics, liveData);
        recyclerView.setAdapter(topicAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(topicAdapter);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setHasFixedSize(true);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseManager.isLoged()){
                    showCreateTopicDialog();
                }
                else {
                    Toast.makeText(getActivity(), "Please log/sign in", Toast.LENGTH_SHORT).show();
                }
            }
        });

/*        if(FirebaseManager.isLoged()){
            if(liveData.getTopics() == null){
                DatabaseManager.getHomeTopics(FirebaseAuth.getInstance().getCurrentUser().getUid(), dbTopics, new DatabaseManager.Listener() {
                    @Override
                    public void onSuccess() {
                        liveData.setTopics(dbTopics);
                        initTopicDataHolderList(dbTopics,topicDataHolderList);
                        liveData.setTopicDataHolderList(topicDataHolderList);
                        Collections.sort(topicDataHolderList);
                        Collections.sort(dbTopics);
                        topicAdapter.setTopics(topicDataHolderList);
                        topicAdapter.notifyDataSetChanged();
                    }
                });

                Log.d("HomeFragment", "livedata set list");
            }
            else {
                dbTopics = liveData.getTopics();
                topicDataHolderList = liveData.getTopicDataHolderList();
                Collections.sort(topicDataHolderList);
                Collections.sort(dbTopics);
                topicAdapter.setTopics(topicDataHolderList);
                topicAdapter.notifyDataSetChanged();
            }
        }*/
        getGames();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initTopicDataHolderList(List<ParentData> topics, List<TopicDataHolder> topicDataHolderList) {


        // Collections.sort(topics);

        for (ParentData topic : topics) {

            List<CommentDataHolder> commentDataHolderList = new ArrayList<>();


            final TopicDataHolder dataHolder = new TopicDataHolder(topic.getTitle(), topic.getTimestamp(), commentDataHolderList, topic.getGame_key(), topic.getGame_key(), topic.getId());
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

            for (final ChildData comment : topic.getItems()) {

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
            Collections.sort(topicDataHolderList);
            topicAdapter.notifyDataSetChanged();
        }
    }

    private void showCreateTopicDialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_create_new_topic);

        dialog.setTitle("New Topic Dialog");

        final TextInputEditText topicEt = dialog.findViewById(R.id.create_new_topic_dialog_topic_name_edit_text);
        final TextInputEditText massageEt = dialog.findViewById(R.id.create_new_topic_dialog_massage_edit_text);
        MaterialButton sendBtn = dialog.findViewById(R.id.create_new_topic_dialog_imageBtn);
        recyclerView = dialog.findViewById(R.id.create_new_topic_dialog_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        //recyclerView.setLayoutManager(new GridLayoutManager((getContext()),2));

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

            @Override
            public void onLongClick(View view, int position) {

            }
        });

        recyclerView.setAdapter(gameAdapter);
        recyclerView.setHasFixedSize(true);
        gameAdapter.notifyDataSetChanged();

        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(), gameDataList, gameAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (massageEt.getText().toString().equals("") || topicEt.getText().toString().equals("")) {
                    Snackbar.make(view, getText(R.string.not_choose), Snackbar.LENGTH_LONG).show();
                } else if (chosenGame.getGuid() == null) {
                    Snackbar.make(view, getString(R.string.choose_game), Snackbar.LENGTH_LONG).show();

                } else {
                    List<ChildData> comments = new ArrayList<>();
                    comments.add(new ChildData(massageEt.getText().toString(), System.currentTimeMillis(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    final ParentData topic = new ParentData(topicEt.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), chosenGame.getGuid(), comments);
                    DatabaseManager.addTopicToDatabase(chosenGame.getGuid(), topic);
                    List<CommentDataHolder> commentDataHolderList = new ArrayList<>();
                    final CommentDataHolder commentDataHolder = new CommentDataHolder();
                    commentDataHolder.setMassege(massageEt.getText().toString());

                    TopicDataHolder topicDataHolder = new TopicDataHolder(topic.getTitle(), System.currentTimeMillis(), commentDataHolderList, FirebaseManager.getCurrentUserId(), chosenGame.getGuid(), topic.getId());
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
                    topicDataHolderList.add(0, topicDataHolder);
                    dbTopics.add(0, new ParentData(topicDataHolder.getTitle(), topicDataHolder.getUserId(), topicDataHolder.getGameId(), comments));
                    topicAdapter.notifyDataSetChanged();
                    dialog.dismiss();


                }
            }
        });


        dialog.show();
    }

    private void getGames() {
        if (FirebaseManager.isLoged()) {
            DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
                @Override
                public void onSuccess(UserData userData) {
                    List<String> gamesIDs = userData.getGames();
                    Log.d("HomeFragment", "getGame - " + gamesIDs);

                    loadDBTopics(gamesIDs);

                }
            });
        } else {
            SharedPreferences sp = getActivity().getSharedPreferences("sp", 0);
            Gson gson = new Gson();
            String json = sp.getString("game_list", "");
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> localUserGameList = gson.fromJson(json, type);

            if(localUserGameList != null){
                loadDBTopics(localUserGameList);

            }

        }
    }

    private void loadDBTopics(List<String> gamesIds) {

        DatabaseManager.getAllTopicsByGameListFromDatabase(gamesIds, new DatabaseManager.DataListener<List<ParentData>>() {
            @Override
            public void onSuccess(List<ParentData> parentData) {
                Log.d("HomeFragment", "loadDBTopics - " + parentData);

                dbTopics.addAll(parentData);
                Collections.sort(dbTopics);
                loadLocalTopics();
            }
        });

    }

    private void loadLocalTopics() {

        initTopicDataHolderList(dbTopics, topicDataHolderList);

    }


}
