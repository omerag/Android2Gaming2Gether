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

import java.util.ArrayList;
import java.util.List;

import hit.android2.Adapters.UserAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.UserData;

public class GroupMessagesFragment extends Fragment {

    FloatingActionButton addNewGroupBtn;
    FirebaseManager manager = new FirebaseManager();

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

            List<UserData> friends = new ArrayList<>();

            final EditText groupName = dialog.findViewById(R.id.group_name_ET);
            final RecyclerView friendsRecycler = dialog.findViewById(R.id.friends_recycler_view);
            friendsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            final UserAdapter userAdapter = new UserAdapter(getContext(),friends);

            DatabaseManager.getUserFriends(manager.getFireBaseAuth().getCurrentUser().getUid(), friends, new DatabaseManager.Listener() {
                @Override
                public void onSuccess() {

                    friendsRecycler.setAdapter(userAdapter);
                    friendsRecycler.setHasFixedSize(true);
                    userAdapter.notifyDataSetChanged();
                }
            });

            dialog.show();
        }
    }
}
