package hit.android2;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.UserAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.UserData;

public class GroupMessagesFragment extends Fragment {

    FloatingActionButton addNewGroupBtn;
    FirebaseManager manager = new FirebaseManager();
    private List<String> group_users_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_chat_fragment, container, false);
        addNewGroupBtn = view.findViewById(R.id.floating_action_btn);
        AddNewGroupBtnListener newGroupBtnListener = new AddNewGroupBtnListener();
        addNewGroupBtn.setOnClickListener(newGroupBtnListener);
        return view;
    }

    public class AddNewGroupBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            final Dialog dialog = new Dialog(getActivity());

            dialog.setContentView(R.layout.new_group_dialog);
            dialog.setTitle("Create Group");

            final List<UserData> friends = new ArrayList<>();

            final EditText groupNameET = dialog.findViewById(R.id.group_name_ET);
            final RecyclerView friendsRecycler = dialog.findViewById(R.id.friends_recycler_view);
            friendsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            final UserAdapter userAdapter = new UserAdapter(getContext(),friends);
            final Button createGroupBtn = dialog.findViewById(R.id.create_group_btn);
            group_users_id = new ArrayList<>();

            DatabaseManager.getUserFriends(manager.getFireBaseAuth().getCurrentUser().getUid(), friends, new DatabaseManager.Listener() {
                @Override
                public void onSuccess() {

                    friendsRecycler.setAdapter(userAdapter);
                    friendsRecycler.setHasFixedSize(true);
                    userAdapter.notifyDataSetChanged();
                }
            });

            userAdapter.setListener(new UserAdapter.AdapterListener() {
                @Override
                public void onClick(View view, int position) {

                    group_users_id.add(friends.get(position).getKey());
                }
            });

            createGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String group_name = groupNameET.getText().toString();

                    if (TextUtils.isEmpty(group_name))
                    {
                        Toast.makeText(getContext(), "Enter group name...", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        group_users_id.add(manager.getFireBaseAuth().getCurrentUser().getUid());
                        createNewGroup(group_name);
                        dialog.dismiss();
                    }
                }
            });

            dialog.setCanceledOnTouchOutside(false);

            dialog.show();
        }
    }

    private void createNewGroup(final String group_name) {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups").child(group_name);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    reference.setValue("");
                    addGroupToUsersChatList(group_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addGroupToUsersChatList(final String group_name) {

        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child("GroupChatList");

        for (int i = 0; i < group_users_id.size(); i++){

            groupRef.child(group_users_id.get(i)).push().setValue(group_name);
        }

    }
}
