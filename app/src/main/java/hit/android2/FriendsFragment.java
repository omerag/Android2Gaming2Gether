package hit.android2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.UserData;
import hit.android2.Adapters.UserAdapter;

public class FriendsFragment extends Fragment {

    private UserAdapter userAdapter;
    private List<UserData> friendsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_fragment,container,false);


        recyclerView =rootView.findViewById(R.id.friends_fragment_recycler_users);
        floatingActionButton = rootView.findViewById(R.id.friends_fragment_floating_action_btn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        userAdapter = new UserAdapter(getActivity(), friendsList); //friendsList is empty, needs to be loaded from server

        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();

        userAdapter.setListener(new UserAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                intent.putExtra("user_id", friendsList.get(position).getKey());
                getActivity().startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFriendDialog();
            }
        });


        loadFriends();


        return rootView;
    }

    private void loadFriends(){
        DatabaseManager.getUserFriends(FirebaseAuth.getInstance().getCurrentUser().getUid(),friendsList,userAdapter);


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
        GameAdapter gameAdapter = new GameAdapter(getActivity(),games,"");
        final UserAdapter userAdapter = new UserAdapter(getActivity(),users);
        recycler.setAdapter(gameAdapter);

        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                gameGUID[0] = games.get(position).getGuid();
                recycler.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
                DatabaseManager.searchPlayers(gameGUID[0],users,userAdapter);

            }
        });


        gameAdapter.notifyDataSetChanged();

        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(),games,gameAdapter);
        dialog.show();
    }


}
