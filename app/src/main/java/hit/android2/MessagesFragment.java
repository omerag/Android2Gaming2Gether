package hit.android2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.MessagesListAdapter;
import hit.android2.Adapters.UserAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.UserData;
import hit.android2.Model.Chatlist;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessagesListAdapter messagesListAdapter;
    private List<UserData> mUsers;
    private List<Chatlist> usersList;

    private MassagesFragmentLiveData liveData;

    FirebaseUser fuser;
    FirebaseManager manager = new FirebaseManager();
    DatabaseReference reference;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.messages_fragment, container, false);

        liveData = ViewModelProviders.of(this).get(MassagesFragmentLiveData.class);


        recyclerView = rootView.findViewById(R.id.messages_fragment_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        messagesListAdapter = new MessagesListAdapter(getContext(), mUsers);
        recyclerView.setAdapter(messagesListAdapter);

        fuser = manager.getFireBaseAuth().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child("userChatList").child(fuser.getUid());

        messagesListAdapter.setListener(new MessagesListAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                intent.putExtra("user_id", mUsers.get(position).getKey());
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readChats()
    {
        if(liveData.getmUsers() == null){
            mUsers.clear();
            DatabaseManager.getUsersFromList(usersList, mUsers, new DatabaseManager.Listener() {
                @Override
                public void onSuccess() {
                    liveData.setmUsers(mUsers);
                    messagesListAdapter.notifyDataSetChanged();
                }
            });
        }
        else {
            mUsers = liveData.getmUsers();
            messagesListAdapter.setUserDataList(mUsers);
            messagesListAdapter.notifyDataSetChanged();
        }

    }

}
