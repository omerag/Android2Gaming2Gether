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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Database.DatabaseManager;
import hit.android2.gaintbomb.api.DataLoader;
import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.GameData;

public class ProfileFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    private List<GameData> gameDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;

    private boolean isLogIn = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        floatingActionButton = rootView.findViewById(R.id.floating_action_btn);

        FloatingBtnListener floatingBtnListener = new FloatingBtnListener();
        floatingActionButton.setOnClickListener(floatingBtnListener);


        RecyclerView recyclerView =rootView.findViewById(R.id.profile_fragment_recycler_games);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        gameAdapter = new GameAdapter(getActivity(),gameDataList,"profile"); //gameDataList is empty, needs to be loaded from server

        recyclerView.setAdapter(gameAdapter);
        gameAdapter.notifyDataSetChanged();

        loadUserGames();


        return rootView;
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

        dialog.setContentView(R.layout.search_dialog_layout);

        dialog.setTitle("Search Dialog");

        final EditText searchText = dialog.findViewById(R.id.search_dialog_edit_text);
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
        DatabaseManager.getUserGames(FirebaseAuth.getInstance().getCurrentUser().getUid(),gameDataList,gameAdapter);
    }

}
