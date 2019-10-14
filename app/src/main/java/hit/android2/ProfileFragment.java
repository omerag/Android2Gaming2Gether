package hit.android2;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Database.DatabaseManager;
import hit.android2.Database.Model.UserData;
import hit.android2.gaintbomb.api.DataLoader;
import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.Model.GameData;

public class ProfileFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    private TextView usernameTv;
    private ImageView userIv;

    private List<GameData> gameDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;

    private ProfileFragmentLiveData liveData;

    private boolean isLogIn = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        liveData = ViewModelProviders.of(this).get(ProfileFragmentLiveData.class);

        floatingActionButton = getView().findViewById(R.id.floating_action_btn);
        usernameTv = getView().findViewById(R.id.profile_fragment_user_name);
        userIv = getView().findViewById(R.id.user_profile_img);

        FloatingBtnListener floatingBtnListener = new FloatingBtnListener();
        floatingActionButton.setOnClickListener(floatingBtnListener);


        RecyclerView recyclerView =getView().findViewById(R.id.profile_fragment_recycler_games);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        gameAdapter = new GameAdapter(getActivity(),gameDataList,"profile"); //gameDataList is empty, needs to be loaded from server

        recyclerView.setAdapter(gameAdapter);
        gameAdapter.notifyDataSetChanged();

        loadUserGames();
    }


    class FloatingBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showSearchDialog();

           // Toast.makeText(getContext(), "Action Clicked", Toast.LENGTH_LONG).show();
        }
    }

    private void showSearchDialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.search_game_dialog_layout);

        dialog.setTitle("Search Dialog");

        final EditText searchText = dialog.findViewById(R.id.search_game_dialog_edit_text);
        final ImageButton searchBtn = dialog.findViewById(R.id.search_button);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView = dialog.findViewById(R.id.search_dialog_recycler_view);

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                List<GameData> gameDataList = new ArrayList<>();
                gameAdapter = new GameAdapter(getContext(), gameDataList,"search game");

                recyclerView.setAdapter(gameAdapter);
                recyclerView.setHasFixedSize(true);
                gameAdapter.notifyDataSetChanged();

                DataLoader loader = new DataLoader(BuildConfig.GiantBombApi,getContext(), gameDataList, gameAdapter);

                loader.searchGameRequest(searchText.getText().toString());

                //searchBtn.setVisibility(View.GONE);
            }
        });

        dialog.show();
    }

    private void loadUserGames(){

        if(liveData.getUserIv() == null || liveData.getUsernameTv() == null){
            final UserData user = new UserData();
            DatabaseManager.getUserFromDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid(), user, usernameTv, userIv, getActivity(), new DatabaseManager.Listener() {
                @Override
                public void onSuccess(Object object) {

                    liveData.setUsernameTv(user.getName());
                    liveData.setUserIv(user.getImageUrl());

                }
            });
        }
        else {
            usernameTv.setText(liveData.getUsernameTv());
            Glide.with(getActivity()).load(liveData.getUserIv()).into(userIv);
        }
        if(liveData.getGameDataList() == null ){
            DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(), gameDataList, gameAdapter, new DatabaseManager.Listener() {
                @Override
                public void onSuccess(Object object) {
                    liveData.setGameDataList(gameDataList);

                    Log.d("ProfileFragment","Loading List from server");
                }
            });
        }
        else {
            gameDataList = liveData.getGameDataList();
            gameAdapter.setGameDataList(gameDataList);
            gameAdapter.notifyDataSetChanged();
        }
    }

}
