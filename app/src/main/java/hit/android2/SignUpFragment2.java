package hit.android2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;

public class SignUpFragment2 extends Fragment {

    private BottomNavigationView navigationView;
    private ViewPager pager;

    private DatePicker birth_day_picker;
    private Button skip_update_btn;
    private Button update_profile_btn;
    private ImageButton back_btn;
    private EditText about_me_et;

    private String birthday;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_fragment2, container, false);

        birth_day_picker = rootView.findViewById(R.id.birth_day_picker);
        skip_update_btn = rootView.findViewById(R.id.skip_profile_update_btn);
        update_profile_btn = rootView.findViewById(R.id.update_profile_btn);
        back_btn = rootView.findViewById(R.id.back_btn);
        about_me_et = rootView.findViewById(R.id.about_me_et);


        SkipUpdateBtnListener skipUpdateBtnListener = new SkipUpdateBtnListener();
        skip_update_btn.setOnClickListener(skipUpdateBtnListener);

        UpdateProfileBtnListener updateProfileBtnListener = new UpdateProfileBtnListener();
        update_profile_btn.setOnClickListener(updateProfileBtnListener);

        BackBtnListener backBtnListener = new BackBtnListener();
        back_btn.setOnClickListener(backBtnListener);

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

    public class UpdateProfileBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {


            String monthStr = "";
            String dayStr = "";

            int year = birth_day_picker.getYear();
            int month = birth_day_picker.getMonth() + 1;
            int day = birth_day_picker.getDayOfMonth();

            if (month < 10)
            {
                monthStr = "0" + month;
            }
            if (day < 10)
            {
                dayStr = "0" + day;
            }
            birthday = year + "/" + monthStr + "/" + dayStr;
            Log.d("myBirthday", birthday);

            String about_me = about_me_et.getText().toString();

            DatabaseManager.updateUserData(FirebaseManager.getCurrentUserId(),about_me,null,birthday,null,0,0);
        }
    }

    public class BackBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();
            navigationView.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
        }
    }
}
