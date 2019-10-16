package hit.android2;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.CharacterSelectAdapter;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.FirebaseManager;
import hit.android2.Database.Model.UserData;
import hit.android2.gaintbomb.api.DataLoader;
import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.Model.GameData;

public class ProfileFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    private TextView usernameTv;
    private ImageView userIv;
    private ImageButton pic_edit_btn;

    private List<GameData> gameDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;

    private ProfileFragmentLiveData liveData;

    private boolean isLogIn = false;

    private boolean isGameRecycleOnScreen = true;



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
        pic_edit_btn = getView().findViewById(R.id.image_edit_btn);

        FloatingBtnListener floatingBtnListener = new FloatingBtnListener();
        floatingActionButton.setOnClickListener(floatingBtnListener);

        ProfileImageEditListener profileImageEditListener = new ProfileImageEditListener();
        pic_edit_btn.setOnClickListener(profileImageEditListener);


        RecyclerView recyclerView =getView().findViewById(R.id.profile_fragment_recycler_games);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        gameAdapter = new GameAdapter(getActivity(),gameDataList); //gameDataList is empty, needs to be loaded from server

        recyclerView.setAdapter(gameAdapter);
        gameAdapter.notifyDataSetChanged();
        if(FirebaseManager.isLoged()){
            loadUserGames();
        }
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

                final List<GameData> gameDataList = new ArrayList<>();
                gameAdapter = new GameAdapter(getContext(), gameDataList);
                gameAdapter.setListener(new GameAdapter.AdapterListener() {
                    @Override
                    public void onClick(View view, int position) {
                        DatabaseManager.userAddGame(FirebaseAuth.getInstance().getCurrentUser().getUid(), gameDataList.get(position).getGuid());
                        DatabaseManager.addGameToDatabase(gameDataList.get(position));
                    }
                });

                recyclerView.setAdapter(gameAdapter);
                recyclerView.setHasFixedSize(true);
                gameAdapter.notifyDataSetChanged();

                DataLoader loader = new DataLoader(BuildConfig.GiantBombApi,getContext());

                loader.searchGameRequest(searchText.getText().toString(),gameDataList,gameAdapter);

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
                public void onSuccess() {

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
                public void onSuccess() {
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

    class ProfileImageEditListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {

            PopupMenu popupMenu = new PopupMenu(getContext(),pic_edit_btn);
            popupMenu.getMenuInflater().inflate(R.menu.poupup_menu,popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId())
                    {
                        case R.id.menu_item_camera:
                            break;
                        case R.id.menu_item_gallery:
                            break;
                        case R.id.menu_item_figures:
                            createCharacterImageSelectDialog();
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
    }

    void createCharacterImageSelectDialog(){


        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_select_character_image);

        dialog.setTitle("Select Character Dialog");


        final RecyclerView recyclerView = dialog.findViewById(R.id.dialog_character_select_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        final List<String> characterUrlList = new ArrayList<>();
        final CharacterSelectAdapter characterSelectAdapter = new CharacterSelectAdapter(dialog.getContext(),characterUrlList);
        characterSelectAdapter.setListener(new CharacterSelectAdapter.Listener() {
            @Override
            public void onClick(String s) {
                DatabaseManager.updateProfileImage(FirebaseAuth.getInstance().getCurrentUser().getUid(),s);
                dialog.dismiss();
            }
        });


        GameAdapter gameAdapter = new GameAdapter(dialog.getContext(), gameDataList);
        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                DataLoader loader = new DataLoader(BuildConfig.GiantBombApi,dialog.getContext());
                loader.getCharactersByGameRequest(gameDataList.get(position).getGuid(), new DataLoader.Listener() {
                    @Override
                    public void onSuccess(String string) {

                        if(isGameRecycleOnScreen){
                            recyclerView.setAdapter(characterSelectAdapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(dialog.getContext(),4));
                            isGameRecycleOnScreen = false;
                        }


                        characterUrlList.add(string);
                        characterSelectAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
        recyclerView.setAdapter(gameAdapter);
        gameAdapter.notifyDataSetChanged();
        dialog.show();
    }
}
