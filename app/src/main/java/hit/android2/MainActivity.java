package hit.android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String userName;
    FirebaseManager fireBaseManager = new FirebaseManager();

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

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTv = headerView.findViewById(R.id.nav_header_user_name);
        fireBaseManager.setReference(navigationView, userNameTv);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

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
                    selectedFragment = new MessagesFragment();
                    break;
                case R.id.nav_friends:
                    selectedFragment = new FriendsFragment();
                    break;
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
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
        fireBaseManager.registerAuthListener();
        // fireBaseManager.getFireBaseAuth().addAuthStateListener(fireBaseManager.getAuthStateListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireBaseManager.unRegisterAuthListener();
        // fireBaseManager.getFireBaseAuth().removeAuthStateListener(fireBaseManager.getAuthStateListener());
    }

}
