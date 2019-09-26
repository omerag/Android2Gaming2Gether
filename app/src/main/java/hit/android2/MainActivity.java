package hit.android2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavBarListener bottomNavBarListener = new bottomNavBarListener();
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavBarListener);
    }

    class bottomNavBarListener implements BottomNavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId())
            {
                case R.id.nav_profile:
                    break;
                case R.id.nav_messages:
                    break;
            }

            return true;
        }
    }

}
