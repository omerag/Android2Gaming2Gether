package hit.android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Adapters.MessageAdapter;
import hit.android2.Database.FirebaseManager;
import hit.android2.Database.Model.UserData;
import hit.android2.Model.Chat;

import static hit.android2.Database.DatabaseManager.getUserFromDatabase;

public class MessagingActivity extends AppCompatActivity {

    CircleImageView profile_img;
    TextView user_name;
    ImageButton send_btn;
    EditText text_send;
    String userId;

    FirebaseUser fuser;
    DatabaseReference reference;
    FirebaseManager firebaseManager;
    UserData userData;

    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.messages_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_img = findViewById(R.id.profile_image);
        user_name = findViewById(R.id.username);
        send_btn = findViewById(R.id.send_btn);
        text_send = findViewById(R.id.text_send);

        userData = new UserData();
        firebaseManager = new FirebaseManager();

        userId = getIntent().getStringExtra("user_id");

        fuser = firebaseManager.getFireBaseAuth().getCurrentUser();
        getUserFromDatabase(userId, userData, user_name, profile_img,MessagingActivity.this);
        readMessages(fuser.getUid(), userId, userData.getImageUrl());

        SendBtnListener sendBtnListener = new SendBtnListener();
        send_btn.setOnClickListener(sendBtnListener);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class SendBtnListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            String msg = text_send.getText().toString();
            if (!msg.equals("")){
                firebaseManager.sendMessage(fuser.getUid(), userId, msg);
            }

            text_send.setText("");
        }
    }

    private void readMessages(final String myId, final String friendId, final String imageUrl){

        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChat.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(friendId) ||
                        chat.getReceiver().equals(friendId) && chat.getSender().equals(myId))
                    {
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessagingActivity.this, mChat, imageUrl);
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
