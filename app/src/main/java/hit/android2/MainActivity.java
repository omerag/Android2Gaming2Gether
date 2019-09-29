package hit.android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    static NavigationView navigationView;
    static String userName;
    FireBaseManager fireBaseManager = new FireBaseManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        NavigationViewListener navigationViewListener = new NavigationViewListener();
        navigationView.setNavigationItemSelectedListener(navigationViewListener);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavBarListener bottomNavBarListener = new bottomNavBarListener();
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavBarListener);

    }


    //Bottom navigation menu listener class
    class bottomNavBarListener implements BottomNavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.nav_messages:
                    break;
            }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            return true;
        }
    }


    //Drawer menu listener class
    class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            drawerLayout.closeDrawers();

            switch (menuItem.getItemId())
            {
                case R.id.sign_up:
                    showSignUpDialog();
                    break;
                case R.id.log_in:
                    showLogInDialog();
                    break;
                case R.id.log_out:
                    fireBaseManager.logOutUser();
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSignUpDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.sign_up_dialog,null);

        final EditText userNameInput = dialogView.findViewById(R.id.sign_up_username_et);
        final EditText emailInput = dialogView.findViewById(R.id.sign_up_email_et);
        final EditText passwordInput = dialogView.findViewById(R.id.sign_up_password_et);

        builder.setView(dialogView).setPositiveButton(R.string.drawer_sign_up, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                userName = userNameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                fireBaseManager.signUpUser(userName, email, password);
            }
        }).show();
    }

    private void showLogInDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.log_in_dialog,null);

        final EditText emailInput = dialogView.findViewById(R.id.log_in_email_et);
        final EditText passwordInput = dialogView.findViewById(R.id.log_in_password_et);

        builder.setView(dialogView).setPositiveButton(R.string.drawer_log_in, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                fireBaseManager.logInUser(email,password);
            }
        }).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBaseManager.getFireBaseAuth().addAuthStateListener(fireBaseManager.getAuthStateListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireBaseManager.getFireBaseAuth().removeAuthStateListener(fireBaseManager.getAuthStateListener());
    }

    public static class AuthStateChangedListener implements FirebaseAuth.AuthStateListener{
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            View headerView = navigationView.getHeaderView(0);
            TextView userNameTv = headerView.findViewById(R.id.nav_header_user_name);

            FirebaseUser currUser = firebaseAuth.getCurrentUser();

            if (currUser != null)
            {
                if (userName != null)
                {
                    currUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            userName = null;
                        }
                    });
                }

                userNameTv.setText(currUser.getDisplayName());
                navigationView.getMenu().findItem(R.id.sign_up).setVisible(false);
                navigationView.getMenu().findItem(R.id.log_in).setVisible(false);
                navigationView.getMenu().findItem(R.id.log_out).setVisible(true);
            }
            else {
                userNameTv.setText(R.string.user_name_tv);
                navigationView.getMenu().findItem(R.id.sign_up).setVisible(true);
                navigationView.getMenu().findItem(R.id.log_in).setVisible(true);
                navigationView.getMenu().findItem(R.id.log_out).setVisible(false);
            }

        }
    }

}
