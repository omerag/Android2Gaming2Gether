package hit.android2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SignUpFragment2 extends Fragment {

    private BottomNavigationView navigationView;
    private ViewPager pager;

    private Button skip_update_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_fragment2, container, false);


        skip_update_btn = rootView.findViewById(R.id.skip_profile_update_btn);


        SkipUpdateBtnListener skipUpdateBtnListener = new SkipUpdateBtnListener();
        skip_update_btn.setOnClickListener(skipUpdateBtnListener);


        return rootView;
    }

    public SignUpFragment2(BottomNavigationView bottomNavigationView, ViewPager pager) {

        this.navigationView = bottomNavigationView;
        this.pager = pager;
    }

    public class SkipUpdateBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            getActivity().getSupportFragmentManager().popBackStack();
            navigationView.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);

        }
    }
}
