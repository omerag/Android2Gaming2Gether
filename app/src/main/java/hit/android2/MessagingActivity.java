package hit.android2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.FirebaseManager;
import hit.android2.Database.UserData;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_img = findViewById(R.id.profile_image);
        user_name = findViewById(R.id.username);
        send_btn = findViewById(R.id.send_btn);
        text_send = findViewById(R.id.text_send);

        userData = new UserData();
        firebaseManager = new FirebaseManager();

        userId = getIntent().getStringExtra("user_id");

        fuser = firebaseManager.getFireBaseAuth().getCurrentUser();
        getUserFromDatabase(userId, userData, user_name, profile_img,MessagingActivity.this);

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
}
