package hit.android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Managers.MessegingManager;
import hit.android2.Database.Model.UserData;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    CircleImageView profile_imageView;
    ViewPager pager;
    String userName;
    FirebaseManager fireBaseManager = new FirebaseManager();

    PagerAdapter pagerAdapter;

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


        //oadUserPicture();

        initPager();
    }

    void initPager(){


        if(pagerAdapter != null){
            pager.setCurrentItem(3);
        }
        if (Locale.getDefault().toString().equals("iw_IL")) pager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(),1);
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
        profile_imageView = headerView.findViewById(R.id.drawer_profile_picture);
        fireBaseManager.setReference(navigationView, userNameTv, getApplicationContext(), profile_imageView);

        pager.setCurrentItem(2);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }


    //Bottom navigation menu listener class
    class bottomNavBarListener implements BottomNavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId())
            {
                case R.id.nav_profile:
                    pager.setCurrentItem(3);
                    break;
                case R.id.nav_messages:
                    pager.setCurrentItem(0);
                    break;
                case R.id.nav_friends:
                    pager.setCurrentItem(1);
                    break;
                case R.id.nav_home:
                    pager.setCurrentItem(2);
                    break;
            }


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
                    initPager();

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

        if(FirebaseManager.isLoged()){
            fireBaseManager.registerAuthListener();

            MessegingManager.subscribeToTopic(FirebaseManager.getCurrentUserId());
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    System.out.println("messege receive");

                }
            };
            MessegingManager.registerReceiver(this,receiver);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(FirebaseManager.isLoged()){
            fireBaseManager.unRegisterAuthListener();
            MessegingManager.unRegisterReciver(this,receiver);
        }
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

    /*public void loadUserPicture()
    {
        if (FirebaseManager.isLoged())
        {
            FirebaseManager manager = new FirebaseManager();

            DatabaseManager.getUserFromDatabase(manager.getFireBaseAuth().getCurrentUser().getUid(), new DatabaseManager.DataListener<UserData>() {
                @Override
                public void onSuccess(UserData userData) {

                    if (userData.getImageUrl().equals(null))
                    {
                        profile_imageView.setImageResource(R.drawable.blank_profile_img);
                    }
                    else {

                       Glide.with(getApplicationContext()).load(userData.getImageUrl()).into(profile_imageView);
                    }
                }
            });
        }
    }*/
}
