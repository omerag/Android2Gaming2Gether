package hit.android2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.GroupAdapter;
import hit.android2.Adapters.UserAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.GroupData;
import hit.android2.Database.Model.UserData;

public class GroupMessagesFragment extends Fragment {

    private FloatingActionButton addNewGroupBtn;
    private RecyclerView groupsRecyclerview;

    FirebaseManager manager = new FirebaseManager();
    private GroupAdapter groupAdapter;

    private List<String> group_users_id;
    private List<String> mUserGroups;
    private List<GroupData> mGroups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_chat_fragment, container, false);

        addNewGroupBtn = view.findViewById(R.id.floating_action_btn);
        groupsRecyclerview = view.findViewById(R.id.group_messages_fragment_recycler);
        groupsRecyclerview.setHasFixedSize(true);
        groupsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserGroups = new ArrayList<>();
        mGroups = new ArrayList<>();
        groupAdapter = new GroupAdapter(getContext(), mGroups);
        groupsRecyclerview.setAdapter(groupAdapter);

        AddNewGroupBtnListener newGroupBtnListener = new AddNewGroupBtnListener();
        addNewGroupBtn.setOnClickListener(newGroupBtnListener);

        getUserGroups();

        groupAdapter.setListener(new GroupAdapter.GroupAdapterListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MessagingActivity.class);

                getActivity().startActivity(intent);
            }
        });

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
                        String image_URL = "https://cdn.images.express.co.uk/img/dynamic/galleries/517x/370884.jpg";
                        String myId = manager.getFireBaseAuth().getCurrentUser().getUid();
                        group_users_id.add(myId);
                        GroupData groupData = new GroupData(group_name,image_URL,group_users_id);
                        DatabaseManager.addGroupToDatabase(myId, groupData, new DatabaseManager.DataListener<String>() {
                            @Override
                            public void onSuccess(String s) {

                                Snackbar.make(getView(),"Group created", 3000).show();
                                addGroupToUsersChatList(s);
                            }
                        });
                        dialog.dismiss();
                    }
                }
            });

            dialog.setCanceledOnTouchOutside(false);

            dialog.show();
        }
    }


    private void addGroupToUsersChatList(final String group_id) {

        for (int i = 0; i < group_users_id.size(); i++){

            DatabaseManager.addGroupToUser(group_users_id.get(i), group_id);

        }
    }

    private void loadGroups() {


       DatabaseManager.getUserGroupsFromFS(mUserGroups, new DatabaseManager.DataListener<List<GroupData>>() {
           @Override
           public void onSuccess(List<GroupData> groupData) {

                mGroups = groupData;
                groupAdapter.setGroupDataList(mGroups);
                groupAdapter.notifyDataSetChanged();

               Log.d("GroupsSize",groupData.size()+"");
           }
       });
    }

    private void getUserGroups()
    {
        String myId = manager.getFireBaseAuth().getCurrentUser().getUid();

        DatabaseManager.getUserFromDatabase(myId, new DatabaseManager.DataListener<UserData>() {
            @Override
            public void onSuccess(UserData userData) {

                mUserGroups = userData.getGroups();
                loadGroups();
            }
        });
    }
}
