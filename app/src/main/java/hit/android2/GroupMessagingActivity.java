package hit.android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Adapters.GroupMessageAdapter;
import hit.android2.Adapters.MessageAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Managers.MessegingManager;
import hit.android2.Database.Model.UserData;
import hit.android2.Model.Chat;
import hit.android2.Model.GroupChat;

import static hit.android2.Database.Managers.DatabaseManager.getUserFromDatabase;

public class GroupMessagingActivity extends AppCompatActivity {

    CircleImageView group_img;
    TextView group_name;
    ImageButton send_btn;
    EditText text_send;
    String groupId;
    String sender_name;

    FirebaseUser fuser;
    DatabaseReference reference;
    FirebaseManager firebaseManager;
    UserData userData;

    GroupMessageAdapter messageAdapter;
    List<GroupChat> mChat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messaging);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.messages_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        group_img = findViewById(R.id.group_image);
        group_name = findViewById(R.id.group_name);
        send_btn = findViewById(R.id.send_btn);
        text_send = findViewById(R.id.text_send);

        firebaseManager = new FirebaseManager();
        fuser = firebaseManager.getFireBaseAuth().getCurrentUser();

        groupId = getIntent().getStringExtra("groupId");
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("group_image")).into(group_img);
        group_name.setText(getIntent().getStringExtra("group_name"));

        getUserData();
        readMessages(groupId);

        SendBtnListener sendBtnListener = new SendBtnListener();
        send_btn.setOnClickListener(sendBtnListener);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MessegingManager.subscribeToTopic(groupId);
    }

    class SendBtnListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            String msg = text_send.getText().toString();
            if (!msg.equals("")){
                firebaseManager.sendGroupMessage(groupId, fuser.getUid(), sender_name, msg, new FirebaseManager.Listener() {
                    @Override
                    public void onSuccess() {
                        DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
                            @Override
                            public void onSuccess(UserData userData) {
                                MessegingManager.notifyNewMessegInGroup(getApplicationContext(),groupId,group_name.getText().toString(),userData.getName(),msg);

                            }
                        });
                    }
                });
            }

            text_send.setText("");
        }
    }

    private void readMessages(final String groupId){

        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("GroupChats").child(groupId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChat.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    GroupChat chat = snapshot.getValue(GroupChat.class);

                    mChat.add(chat);

                    messageAdapter = new GroupMessageAdapter(GroupMessagingActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserData()
    {
        DatabaseManager.getUserFromDatabase(fuser.getUid(), new DatabaseManager.DataListener<UserData>() {
            @Override
            public void onSuccess(UserData userData) {

                sender_name = userData.getName();
            }
        });
    }

}
