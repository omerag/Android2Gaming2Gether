package hit.android2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditDetailsFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;
    private MenuItem saveEditBtn;

    private ImageButton back_btn;

    private RadioGroup genderSelectGroup;
    private RadioButton genderSelectBtn;

    private LinearLayout calendarBtn;

    private EditText aboutMeEt;
    private EditText addressEt;

    private CheckBox arabic;
    private CheckBox chiness;
    private CheckBox english;
    private CheckBox french;
    private CheckBox german;
    private CheckBox hebrew;
    private CheckBox italian;
    private CheckBox japanese;
    private CheckBox korean;
    private CheckBox russian;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_dialog_edit_details_fragment, container, false);

        back_btn = rootView.findViewById(R.id.back_btn);

        BackBtnListener backBtnListener = new BackBtnListener();
        back_btn.setOnClickListener(backBtnListener);

        genderSelectGroup = rootView.findViewById(R.id.profile_fragment_edit_details_radio_group);
        //genderSelectBtn = rootView.findViewById();

        calendarBtn = rootView.findViewById(R.id.profile_fragment_edit_details_calendar_layout);

        aboutMeEt = rootView.findViewById(R.id.profile_fragment_edit_details_about_me_et);
        addressEt = rootView.findViewById(R.id.profile_fragment_edit_details_address_et);

        arabic = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_arabic);
        chiness = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_chinese);
        english = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_english);
        french = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_french);
        german = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_german);
        hebrew = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_hebrew);
        italian = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_italian);
        japanese = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_japanese);
        korean = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_korean);
        russian = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_russian);

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

    private Map<String,Boolean> setLangueges(){

        Map<String,Boolean> langeuges = new HashMap<>();
        langeuges.put(arabic.getText().toString(),arabic.isChecked());


        return null;
    }
}
