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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Managers.MessegingManager;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    ViewPager pager;
    String userName;
    FirebaseManager fireBaseManager = new FirebaseManager();

    BroadcastReceiver receiver;

    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        pager = findViewById(R.id.fragment_container);
        if (Locale.getDefault().toString().equals("iw_IL")) pager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        PagerAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager(),1);
        pager.setAdapter(pagerAdapter);

        PageViewChangeListener pageViewChangeListener = new PageViewChangeListener();
        pager.addOnPageChangeListener(pageViewChangeListener);


        NavigationViewListener navigationViewListener = new NavigationViewListener();
        navigationView.setNavigationItemSelectedListener(navigationViewListener);

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavBarListener bottomNavBarListener = new bottomNavBarListener();
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavBarListener);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTv = headerView.findViewById(R.id.nav_header_user_name);
        fireBaseManager.setReference(navigationView, userNameTv);

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        pager.setCurrentItem(2);
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
                    //selectedFragment = new ProfileFragment();
                    pager.setCurrentItem(3);
                    break;
                case R.id.nav_messages:
                    //selectedFragment = new MessagesFragment();
                    pager.setCurrentItem(0);
                    break;
                case R.id.nav_friends:
                   // selectedFragment = new FriendsFragment();
                    pager.setCurrentItem(1);
                    break;
                case R.id.nav_home:
                    //selectedFragment = new HomeFragment();
                    pager.setCurrentItem(2);
                    break;
            }

           //     getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

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
                    //showSignUpDialog();
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    {
                        getSupportFragmentManager().popBackStack();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.dialog_fragments_container,new SignUpFragment1(bottomNavigationView, pager))
                            .addToBackStack("signUpFragment").commit();

                    bottomNavigationView.setVisibility(View.GONE);
                    pager.setVisibility(View.INVISIBLE);
                    break;
                case R.id.log_in:
                    //showLogInDialog();

                    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    {
                        getSupportFragmentManager().popBackStack();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.dialog_fragments_container, new LoginFragment(bottomNavigationView, pager))
                            .addToBackStack("loginFragment").commit();

                    bottomNavigationView.setVisibility(View.GONE);
                    pager.setVisibility(View.INVISIBLE);
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


    @Override
    protected void onStart() {
        super.onStart();
        fireBaseManager.registerAuthListener();

        if(FirebaseManager.isLoged()){
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    System.out.println("messege receive");

                }
            };
            MessegingManager.registerReceiver(this,receiver);
        }

        // fireBaseManager.getFireBaseAuth().addAuthStateListener(fireBaseManager.getAuthStateListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireBaseManager.unRegisterAuthListener();
        if(FirebaseManager.isLoged()){
            MessegingManager.unRegisterReciver(this,receiver);
        }
        // fireBaseManager.getFireBaseAuth().removeAuthStateListener(fireBaseManager.getAuthStateListener());
    }

    public static class PageAdapter extends FragmentPagerAdapter{

        public PageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return 4;
        }


    }

    public static class FragmentFactory{

        public static Fragment getFragment(int index){

            switch (index){
                case 0: return new MessagesRootFragment();
                case 1: return new FriendsFragment();
                case 2: return new HomeFragment();
                case 3: return new ProfileFragment();

            }
            return null;
        }

    }

    class PageViewChangeListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null)
                prevMenuItem.setChecked(false);
            else
                bottomNavigationView.getMenu().getItem(0).setChecked(false);

            bottomNavigationView.getMenu().getItem(position).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bottomNavigationView.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);
    }
}

/*private void showSignUpDialog()
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
    }*/

    /*private void showLogInDialog()
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

    }*/
