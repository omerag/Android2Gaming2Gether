package hit.android2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileEditDetailsFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;
    private MenuItem saveEditBtn;

    private ImageButton back_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_dialog_edit_details_fragment, container, false);

        back_btn = rootView.findViewById(R.id.back_btn);

        BackBtnListener backBtnListener = new BackBtnListener();
        back_btn.setOnClickListener(backBtnListener);

        return rootView;
    }

    public ProfileEditDetailsFragment(BottomNavigationView bottomNavigationView, ViewPager pager) {

        this.bottomNavigationView = bottomNavigationView;
        this.pager = pager;
    }

    class BackBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();

            bottomNavigationView.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
        }
    }
}
