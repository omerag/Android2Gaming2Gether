package hit.android2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.FirebaseManager;

public class MessagingActivity extends AppCompatActivity {

    CircleImageView profile_img;
    TextView user_name;

    FirebaseUser fuser;
    DatabaseReference reference;
    FirebaseManager firebaseManager;
    DatabaseManager databaseManager;

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

        String userId = getIntent().getStringExtra("user_id");

        fuser = firebaseManager.getFireBaseAuth().getCurrentUser();




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
