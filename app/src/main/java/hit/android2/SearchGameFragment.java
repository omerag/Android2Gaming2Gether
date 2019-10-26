package hit.android2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.GameData;
import hit.android2.gaintbomb.api.DataLoader;

public class SearchGameFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;

    private EditText searchText;
    private ImageButton searchBtn;
    private RecyclerView gameListRecyclerView;
    private List<GameData> gameDataList;
    private GameAdapter gameAdapter;

    private ProfileFragmentLiveData liveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_game_fragment, container, false);

        searchBtn = rootView.findViewById(R.id.search_button);
        searchText = rootView.findViewById(R.id.search_game_edit_text);
        gameListRecyclerView = rootView.findViewById(R.id.search_game_recycler_view);

        SearchBtnListener searchBtnListener = new SearchBtnListener();
        searchBtn.setOnClickListener(searchBtnListener);

        return rootView;
    }

    public SearchGameFragment(BottomNavigationView bottomNavigationView, ViewPager pager, List<GameData> gameDataList,GameAdapter gameAdapter,ProfileFragmentLiveData liveData) {
        this.bottomNavigationView = bottomNavigationView;
        this.pager = pager;
        this.gameDataList = gameDataList;
        this.gameAdapter = gameAdapter;
        this.liveData = liveData;
    }

    class SearchBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            gameListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            final List<GameData> gameSearchList = new ArrayList<>();
            final GameAdapter gameSearchAdapter = new GameAdapter(getContext(), gameSearchList);

            gameSearchAdapter.setListener(new GameAdapter.AdapterListener() {
                @Override
                public void onClick(View view, int position) {
                    DatabaseManager.userAddGame(FirebaseAuth.getInstance().getCurrentUser().getUid(), gameSearchList.get(position).getGuid(),
                            new DatabaseManager.Listener() {
                                @Override
                                public void onSuccess() {
                                    DatabaseManager.getUserGames(FirebaseManager.getCurrentUserId(), gameDataList, gameAdapter, new DatabaseManager.Listener() {
                                        @Override
                                        public void onSuccess() {
                                            liveData.setGameDataList(gameDataList);

                                            Log.d("ProfileFragment","Loading List from server");
                                        }
                                    });
                                }
                            });
                    DatabaseManager.addGameToDatabase(gameSearchList.get(position));
                }
            });

            gameListRecyclerView.setAdapter(gameSearchAdapter);
            gameListRecyclerView.setHasFixedSize(true);
            gameSearchAdapter.notifyDataSetChanged();

            DataLoader loader = new DataLoader(BuildConfig.GiantBombApi,getContext());

            loader.searchGameRequest(searchText.getText().toString(),gameSearchList,gameSearchAdapter);
        }

        }
    }