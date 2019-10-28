package hit.android2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Adapters.FlagAdapter;
import hit.android2.Adapters.FriendsAdapter;
import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.UserData;
import hit.android2.Adapters.UserAdapter;

public class FriendsFragment extends Fragment {

    private FriendsAdapter friendsAdapter;
    private List<UserData> friendsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private FriendsFragmentLiveData liveData;
    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_fragment,container,false);

        View view = getActivity().findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView = (BottomNavigationView) view;

        View view1 = getActivity().findViewById(R.id.fragment_container);
        pager = (ViewPager) view1;


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        liveData = ViewModelProviders.of(this).get(FriendsFragmentLiveData.class);


        recyclerView =getView().findViewById(R.id.friends_fragment_recycler_users);
        floatingActionButton = getView().findViewById(R.id.friends_fragment_floating_action_btn);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        friendsAdapter = new FriendsAdapter(getActivity(), friendsList);

        recyclerView.setAdapter(friendsAdapter);
        friendsAdapter.notifyDataSetChanged();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FirebaseManager.isLoged()){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.dialog_fragments_container, new SearchFriendFragment(friendsAdapter, friendsList, liveData))
                            .addToBackStack("searchFriendFragment").commit();

                    bottomNavigationView.setVisibility(View.INVISIBLE);
                    pager.setVisibility(View.INVISIBLE);
                }
                else {

                }

            }
        });

        friendsAdapter.setListener(new FriendsAdapter.AdapterListener() {
            @Override
            public void onLongClick(View view, int position) {

                AlertDialog alertDialog= new MaterialAlertDialogBuilder(getContext())
                        .setTitle(R.string.delete_title)
                        .setMessage(R.string.delete_message)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //delete friend
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }

            @Override
            public void onMessageBtnClick(View view, int position) {

                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                intent.putExtra("user_id", friendsList.get(position).getKey());
                getActivity().startActivity(intent);
            }

            @Override
            public void profileBtnClick(View view, int position) {

                ShowFriendProfile(friendsList.get(position).getKey());
            }
        });

        if(FirebaseManager.isLoged()){
            loadFriends();
        }
    }

    private void ShowFriendProfile(String friend_id)
    {
        final Dialog dialog = new Dialog(getActivity());
        final List<UserData> userData = new ArrayList<>();

        dialog.setContentView(R.layout.user_profile_dialog);

        dialog.setTitle("Friend Profile");

        final CircleImageView profile_image = dialog.findViewById(R.id.user_profile_img);
        final TextView user_name = dialog.findViewById(R.id.profile_fragment_user_name);
        final TextView user_about_me = dialog.findViewById(R.id.about_me_tv);
        List<GameData> games = new ArrayList<>();
        final GameAdapter gameAdapter = new GameAdapter(getActivity(),games);
        final RecyclerView recyclerView = dialog.findViewById(R.id.profile_fragment_recycler_games);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);



        DatabaseManager.getUserFromDatabase(friend_id, userData, new DatabaseManager.Listener() {
            @Override
            public void onSuccess() {
                user_name.setText(userData.get(0).getName());
                user_about_me.setText(userData.get(0).getAboutMe());
                Glide.with(getContext()).load(userData.get(0).getImageUrl()).into(profile_image);
            }
        });

        DatabaseManager.getUserGames(friend_id, games, gameAdapter, new DatabaseManager.Listener() {
            @Override
            public void onSuccess() {
                recyclerView.setAdapter(gameAdapter);
                gameAdapter.notifyDataSetChanged();
            }
        });


        dialog.show();
        dialog.getCurrentFocus();
    }

    private void loadFriends(){
        if(liveData.friendsList == null ){
            DatabaseManager.getUserFriends(FirebaseAuth.getInstance().getCurrentUser().getUid(), friendsList, new DatabaseManager.Listener() {
                @Override
                public void onSuccess() {
                    liveData.setFriendsList(friendsList);
                    friendsAdapter.notifyDataSetChanged();
                }
            });
        }
        else {
            friendsList = liveData.getFriendsList();
            friendsAdapter.setUserDataList(friendsList);
            friendsAdapter.notifyDataSetChanged();
        }


    }

    /*private void showSearchFriendDialog(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.search_friends_dialog_layout);
        dialog.setTitle("Search Friends Dialog");

        String generResult = "all";
        final String[] languegeResult = {"english"};
        String rankTypeResult = "totalRank";
        final int[] age = {0};

        ImageButton searchBtn = dialog.findViewById(R.id.search_button);
        searchBtn.setTag(R.id.search_button);


        final GetStringFromImageBtn stringFromImageBtn =  new GetStringFromImageBtn();
        ImageButton maleGenderIbtn = dialog.findViewById(R.id.search_friends_dialog_gender_male);
        maleGenderIbtn.setTag(R.id.search_friends_dialog_gender_male);
        maleGenderIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton femaleGenderIbtn = dialog.findViewById(R.id.search_friends_dialog_gender_female);
        femaleGenderIbtn.setTag(R.id.search_friends_dialog_gender_female);
        femaleGenderIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton allGenderIbtn = dialog.findViewById(R.id.search_friends_dialog_gender_all);
        allGenderIbtn.setTag(R.id.search_friends_dialog_gender_all);
        allGenderIbtn.setOnClickListener(stringFromImageBtn);

        ImageButton totalRankIbtn = dialog.findViewById(R.id.search_friends_dialog_overall_level);
        totalRankIbtn.setTag(R.id.search_friends_dialog_overall_level);
        totalRankIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton teammateRankIbtn = dialog.findViewById(R.id.search_friends_dialog_teammate_level);
        teammateRankIbtn.setTag(R.id.search_friends_dialog_teammate_level);
        teammateRankIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton spotmanRankIbtn = dialog.findViewById(R.id.search_friends_dialog_sportsmanship_level);
        spotmanRankIbtn.setTag(R.id.search_friends_dialog_sportsmanship_level);
        spotmanRankIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton leaderRankIbtn = dialog.findViewById(R.id.search_friends_dialog_leader_level);
        leaderRankIbtn.setTag(R.id.search_friends_dialog_leader_level);
        leaderRankIbtn.setOnClickListener(stringFromImageBtn);

        final TextView ageTv = dialog.findViewById(R.id.search_friends_dialog_age_tv);
        SeekBar ageSeekBat = dialog.findViewById(R.id.search_friends_seekbar_age);
        ageSeekBat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                age[0] = i;
                ageTv.setText( "Age: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });







        ///flags recycler
        final RecyclerView flagsRecycler = dialog.findViewById(R.id.search_friends_dialog_flags_recycler_view);
        flagsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        FlagAdapter flagAdapter = new FlagAdapter();
        flagsRecycler.setAdapter(flagAdapter);
        flagsRecycler.setHasFixedSize(true);

        flagAdapter.setListener(new FlagAdapter.Listener() {
            @Override
            public void onClick(String language) {
                Toast.makeText(getActivity(), language + " clicked", Toast.LENGTH_SHORT).show();
                languegeResult[0] = language;
            }
        });
        flagAdapter.notifyDataSetChanged();
        ///

        ///games recycler
        final RecyclerView gamesRecycler = dialog.findViewById(R.id.search_friends_dialog_games_recycler_view);
        gamesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        final List<UserData> users = new ArrayList<>();
        final List<GameData> games = new ArrayList<>();
        final String[] gameGUID = new String[1];
        GameAdapter gameAdapter = new GameAdapter(getActivity(),games);
        final UserAdapter userSearchAdapter = new UserAdapter(getActivity(),users);
        gamesRecycler.setAdapter(gameAdapter);

        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                gameGUID[0] = games.get(position).getGuid();
                gamesRecycler.setAdapter(userSearchAdapter);
                userSearchAdapter.notifyDataSetChanged();
                //DatabaseManager.searchPlayers(gameGUID[0],users,userSearchAdapter);

                DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
                    @Override
                    public void onSuccess(UserData userData) {
                        float distance = 0;
                        DatabaseManager.searchPlayers(userData,gameGUID[0], languegeResult[0], stringFromImageBtn.getResultGener(), stringFromImageBtn.getResultRank(),age[0],distance,new DatabaseManager.DataListener<List<UserData>>() {
                            @Override
                            public void onSuccess(List<UserData> userData) {
                                users.addAll(userData);
                                userSearchAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

            }
        });
        gameAdapter.notifyDataSetChanged();
        /////////////

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



        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(),games,gameAdapter);
        dialog.show();
        dialog.getCurrentFocus();

    }

    class GetStringFromImageBtn implements View.OnClickListener{


        String resultGener;
        String resultRank;

        public String getResultGener() {
            return resultGener;
        }

        public String getResultRank() {
            return resultRank;
        }

        @Override
        public void onClick(View view) {
            int tag = (int)view.getTag();

            switch (tag){
                case R.id.search_friends_dialog_gender_male:
                    resultGener = "male";
                    Toast.makeText(getActivity(), resultGener + "clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.search_friends_dialog_gender_female:
                    resultGener = "female";
                    Toast.makeText(getActivity(), resultGener + "clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.search_friends_dialog_gender_all:
                    resultGener = "all";
                    Toast.makeText(getActivity(), resultGener + "clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.search_friends_dialog_overall_level:
                    resultRank = "totalRank";
                    Toast.makeText(getActivity(), resultRank + "clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.search_friends_dialog_teammate_level:
                    resultRank = "teammate";
                    Toast.makeText(getActivity(), resultRank + "clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.search_friends_dialog_sportsmanship_level:
                    resultRank = "sportmanship";
                    break;
                case R.id.search_friends_dialog_leader_level:
                    resultRank = "leader";
                    Toast.makeText(getActivity(), resultRank + "clicked", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    }*/

    /*        userAdapter.setListener(new UserAdapter.AdapterListener() {

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
                                ShowFriendProfile(friendsList.get(viewPosition).getKey());
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
        });*/
}
