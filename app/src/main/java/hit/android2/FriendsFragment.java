package hit.android2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Database.DatabaseManager;
import hit.android2.Database.UserData;
import hit.android2.Adapters.UserAdapter;

public class FriendsFragment extends Fragment {

    UserAdapter userAdapter;
    List<UserData> friendsList = new ArrayList<>();
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_fragment,container,false);


        recyclerView =rootView.findViewById(R.id.friends_fragment_recycler_users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        userAdapter = new UserAdapter(getActivity(), friendsList); //friendsList is empty, needs to be loaded from server

        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();


        loadFriends();


        return rootView;
    }

    private void loadFriends(){
        DatabaseManager.getUserFriends(FirebaseAuth.getInstance().getCurrentUser().getUid(),friendsList,userAdapter);


    }


}
