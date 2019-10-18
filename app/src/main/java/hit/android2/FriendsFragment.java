package hit.android2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.FirebaseManager;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.UserData;
import hit.android2.Adapters.UserAdapter;

public class FriendsFragment extends Fragment {

    private UserAdapter userAdapter;
    private List<UserData> friendsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private FriendsFragmentLiveData liveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_fragment,container,false);




        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        liveData = ViewModelProviders.of(this).get(FriendsFragmentLiveData.class);


        recyclerView =getView().findViewById(R.id.friends_fragment_recycler_users);
        floatingActionButton = getView().findViewById(R.id.friends_fragment_floating_action_btn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        userAdapter = new UserAdapter(getActivity(), friendsList); //friendsList is empty, needs to be loaded from server

        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();

        userAdapter.setListener(new UserAdapter.AdapterListener() {

            @Override
            public void onClick(final View view, int position) {

                final int viewPosition = position;
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.friends_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.menu_item_view_profile:
                                break;

                            case R.id.menu_item_send_message:
                                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                                intent.putExtra("user_id", friendsList.get(viewPosition).getKey());
                                getActivity().startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFriendDialog();
            }
        });

        if(FirebaseManager.isLoged()){
            loadFriends();
        }
    }

    private void loadFriends(){
        if(liveData.friendsList == null ){
            DatabaseManager.getUserFriends(FirebaseAuth.getInstance().getCurrentUser().getUid(), friendsList, new DatabaseManager.Listener() {
                @Override
                public void onSuccess() {
                    liveData.setFriendsList(friendsList);
                    userAdapter.notifyDataSetChanged();
                }
            });
        }
        else {
            friendsList = liveData.getFriendsList();
            userAdapter.setUserDataList(friendsList);
            userAdapter.notifyDataSetChanged();
        }


    }

    private void showSearchFriendDialog(){

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.search_friends_dialog_layout);

        dialog.setTitle("Search Friends Dialog");

        ImageButton searchBtn = dialog.findViewById(R.id.search_button);
        final RecyclerView recycler = dialog.findViewById(R.id.search_friends_dialog_recycler_view);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        final List<UserData> users = new ArrayList<>();
        final List<GameData> games = new ArrayList<>();
        final String[] gameGUID = new String[1];
        GameAdapter gameAdapter = new GameAdapter(getActivity(),games);
        final UserAdapter userSearchAdapter = new UserAdapter(getActivity(),users);
        recycler.setAdapter(gameAdapter);

        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                gameGUID[0] = games.get(position).getGuid();
                recycler.setAdapter(userSearchAdapter);
                userSearchAdapter.notifyDataSetChanged();
                DatabaseManager.searchPlayers(gameGUID[0],users,userSearchAdapter);

            }
        });

        userSearchAdapter.setListener(new UserAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {


                DatabaseManager.userAddFriend(FirebaseAuth.getInstance().getCurrentUser().getUid(), users.get(position).getKey(),
                        new DatabaseManager.Listener() {
                            @Override
                            public void onSuccess() {
                                DatabaseManager.getUserFriends(FirebaseAuth.getInstance().getCurrentUser().getUid(), friendsList, new DatabaseManager.Listener() {
                                    @Override
                                    public void onSuccess() {
                                        liveData.setFriendsList(friendsList);
                                        userAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                Toast.makeText(getActivity(), users.get(position).getName() + "was added to friends list", Toast.LENGTH_SHORT).show();
            }
        });


        gameAdapter.notifyDataSetChanged();

        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(),games,gameAdapter);
        dialog.show();
        dialog.getCurrentFocus();
    }


}
