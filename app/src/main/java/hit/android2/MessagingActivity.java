package hit.android2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        userData = new UserData();
        firebaseManager = new FirebaseManager();

        String userId = getIntent().getStringExtra("user_id");
        Log.d("userID", userId);

        fuser = firebaseManager.getFireBaseAuth().getCurrentUser();
        getUserFromDatabase(userId, userData, user_name, profile_img,MessagingActivity.this);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
