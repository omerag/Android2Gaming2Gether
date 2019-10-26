package hit.android2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileEditDetailsFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_dialog_edit_details_fragment, container, false);


        return rootView;
    }

    public ProfileEditDetailsFragment(BottomNavigationView bottomNavigationView, ViewPager pager) {

        this.bottomNavigationView = bottomNavigationView;
        this.pager = pager;
    }
}
