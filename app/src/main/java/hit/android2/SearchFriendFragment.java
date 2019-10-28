package hit.android2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.FlagAdapter;
import hit.android2.Adapters.FriendsAdapter;
import hit.android2.Adapters.FriendsResultAdapter;
import hit.android2.Adapters.GameAdapter;
import hit.android2.Adapters.UserAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.UserData;
import hit.android2.Helpers.LocationHelper;
import ru.alexbykov.nopermission.PermissionHelper;

public class SearchFriendFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private FriendsAdapter userAdapter;
    private List<UserData> friendsList;
    private FriendsFragmentLiveData liveData;
    private LocationHelper helper;
    private ProgressDialog progressDialog;

    private PermissionHelper permissionHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_friends_dialog_layout, container, false);


        permissionHelper = new PermissionHelper(this);


        return rootView;
    }

    public SearchFriendFragment(FriendsAdapter userAdapter, List<UserData> friendsList, FriendsFragmentLiveData liveData) {
        this.userAdapter = userAdapter;
        this.friendsList = friendsList;
        this.liveData = liveData;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String generResult = "all";
        final String[] languegeResult = {"english"};
        String rankTypeResult = "totalRank";
        final int[] age = {0};

        /*ImageButton searchBtn = getView().findViewById(R.id.search_button);
        searchBtn.setTag(R.id.search_button);*/


        final GetStringFromImageBtn stringFromImageBtn = new GetStringFromImageBtn();
        ImageButton maleGenderIbtn = getView().findViewById(R.id.search_friends_dialog_gender_male);
        maleGenderIbtn.setTag(R.id.search_friends_dialog_gender_male);
        maleGenderIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton femaleGenderIbtn = getView().findViewById(R.id.search_friends_dialog_gender_female);
        femaleGenderIbtn.setTag(R.id.search_friends_dialog_gender_female);
        femaleGenderIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton allGenderIbtn = getView().findViewById(R.id.search_friends_dialog_gender_all);
        allGenderIbtn.setTag(R.id.search_friends_dialog_gender_all);
        allGenderIbtn.setOnClickListener(stringFromImageBtn);

        ImageButton totalRankIbtn = getView().findViewById(R.id.search_friends_dialog_overall_level);
        totalRankIbtn.setTag(R.id.search_friends_dialog_overall_level);
        totalRankIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton teammateRankIbtn = getView().findViewById(R.id.search_friends_dialog_teammate_level);
        teammateRankIbtn.setTag(R.id.search_friends_dialog_teammate_level);
        teammateRankIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton spotmanRankIbtn = getView().findViewById(R.id.search_friends_dialog_sportsmanship_level);
        spotmanRankIbtn.setTag(R.id.search_friends_dialog_sportsmanship_level);
        spotmanRankIbtn.setOnClickListener(stringFromImageBtn);
        ImageButton leaderRankIbtn = getView().findViewById(R.id.search_friends_dialog_leader_level);
        leaderRankIbtn.setTag(R.id.search_friends_dialog_leader_level);
        leaderRankIbtn.setOnClickListener(stringFromImageBtn);

        final TextView ageTv = getView().findViewById(R.id.search_friends_dialog_age_tv);
        SeekBar ageSeekBat = getView().findViewById(R.id.search_friends_seekbar_age);
        ageSeekBat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                age[0] = i;
                ageTv.setText("Age: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final TextView distanceTv = view.findViewById(R.id.search_friends_text_view_distance);
        final float[] distance = {0};
        SeekBar distanceSeekbar = view.findViewById(R.id.search_friends_seekbar_distance);
        distanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                distanceTv.setText("Distance(km): " + i);
                distance[0] = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ///flags recycler
        final RecyclerView flagsRecycler = getView().findViewById(R.id.search_friends_dialog_flags_recycler_view);
        flagsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
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
        final RecyclerView gamesRecycler = getView().findViewById(R.id.search_friends_dialog_games_recycler_view);
        gamesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        RecyclerView resultRecycler = getView().findViewById(R.id.results_recycler);
        resultRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));

        final List<UserData> users = new ArrayList<>();
        final List<GameData> games = new ArrayList<>();
        final String[] gameGUID = new String[1];
        GameAdapter gameAdapter = new GameAdapter(getActivity(), games);
        FriendsResultAdapter friendsResultAdapter = new FriendsResultAdapter(getActivity(),users);
        gamesRecycler.setAdapter(gameAdapter);

        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.progress_message));
                progressDialog.setTitle(getString(R.string.progress_title));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                LinearLayout linearLayout = getActivity().findViewById(R.id.root_search_view);
                linearLayout.setVisibility(View.GONE);
                LinearLayout resultLayout = getActivity().findViewById(R.id.results_view);
                resultLayout.setVisibility(View.VISIBLE);

                gameGUID[0] = games.get(position).getGuid();
                resultRecycler.setAdapter(friendsResultAdapter);
                friendsResultAdapter.notifyDataSetChanged();

                //DatabaseManager.searchPlayers(gameGUID[0],users,userSearchAdapter);

                DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
                    @Override
                    public void onSuccess(final UserData userData) {
                        final double mLongitude = 0;
                        final double mLatitude = 0;

                        helper = new LocationHelper(getActivity(), permissionHelper, new LocationHelper.Listener<Double>() {
                            @Override
                            public void onSuccess(double latitude, double longitude) {
                                DatabaseManager.searchPlayers(userData, gameGUID[0], languegeResult[0], stringFromImageBtn.getResultGener(), stringFromImageBtn.getResultRank(), age[0], distance[0] * 1000, latitude, longitude, new DatabaseManager.DataListener<List<UserData>>() {
                                    @Override
                                    public void onSuccess(List<UserData> userData) {
                                        Log.d("SearchFriendsFragment", "onSuccess - userDataList =" + userData.toString());
                                        users.addAll(userData);
                                        friendsResultAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });

                    }
                });

            }
        });
        gameAdapter.notifyDataSetChanged();
        /////////////

        friendsResultAdapter.setListener(new FriendsResultAdapter.AdapterListener() {
            @Override
            public void onAddAsFriendClick(View view, int position) {

                DatabaseManager.userAddFriend(FirebaseAuth.getInstance().getCurrentUser().getUid(), users.get(position).getKey(),
                        new DatabaseManager.Listener() {
                            @Override
                            public void onSuccess() {
                                DatabaseManager.getUserFriends(FirebaseAuth.getInstance().getCurrentUser().getUid(), friendsList, new DatabaseManager.Listener() {
                                    @Override
                                    public void onSuccess() {
                                        liveData.setFriendsList(friendsList);
                                        userAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });

                Snackbar.make(getView(),users.get(position).getName() + " " + getString(R.string.add_friend),3000).show();
            }
        });



        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(), games, gameAdapter);

        super.onViewCreated(view, savedInstanceState);
    }

    class GetStringFromImageBtn implements View.OnClickListener {


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
            int tag = (int) view.getTag();

            switch (tag) {
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("onRequestPermResult","");
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
