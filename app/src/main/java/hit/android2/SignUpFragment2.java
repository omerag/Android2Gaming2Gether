package hit.android2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Helpers.GeoHelper;

public class SignUpFragment2 extends Fragment {

    private BottomNavigationView navigationView;
    private ViewPager pager;

    private Button skip_update_btn;
    private Button update_profile_btn;


    private MainActivity mainActivity;


    private RadioGroup genderSelectGroup;
    private RadioButton genderSelectBtn;
    private String gender;

    private LinearLayout calendarBtn;
    private TextView calendarTv;
    private String birthday;

    private EditText aboutMeEt;
    private EditText addressEt;

    private CheckBox arabic;
    private CheckBox chinese;
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
        View rootView = inflater.inflate(R.layout.sign_up_fragment2, container, false);

        skip_update_btn = rootView.findViewById(R.id.fragment_sign_up2_skip_profile_update_btn);
        update_profile_btn = rootView.findViewById(R.id.sign_up);
        aboutMeEt = rootView.findViewById(R.id.fragment_sign_up2_edit_details_about_me_et);


        SkipUpdateBtnListener skipUpdateBtnListener = new SkipUpdateBtnListener();
        skip_update_btn.setOnClickListener(skipUpdateBtnListener);

        UpdateProfileBtnListener updateProfileBtnListener = new UpdateProfileBtnListener();
        update_profile_btn.setOnClickListener(updateProfileBtnListener);

        ///////////////////////////////////////


        update_profile_btn = rootView.findViewById(R.id.profile_fragment_edit_details_save_changes_btn);
        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                genderSelectBtn = rootView.findViewById(genderSelectGroup.getCheckedRadioButtonId());
                if (genderSelectBtn.getId() == R.id.profile_fragment_edit_details_radio_male) {
                    gender = "male";
                } else if (genderSelectBtn.getId() == R.id.profile_fragment_edit_details_radio_female) {
                    gender = "female";
                } else {
                    gender = "all";
                }


                if(addressEt.getText().toString().equals("")){
                    DatabaseManager.updateUserData(FirebaseManager.getCurrentUserId(), aboutMeEt.getText().toString(), setLangueges(), birthday, gender, 0, 0);
                    Toast.makeText(getActivity(), "changes saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    new GeoHelper(getActivity(), null, addressEt.getText().toString(), new GeoHelper.Listener<Double>() {
                        @Override
                        public void onSuccess(Double latitude, Double longitude) {
                            DatabaseManager.updateUserData(FirebaseManager.getCurrentUserId(), aboutMeEt.getText().toString(), setLangueges(), birthday, gender, latitude, longitude);
                            Toast.makeText(getActivity(), "changes saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        genderSelectGroup = rootView.findViewById(R.id.profile_fragment_edit_details_radio_group);
        //genderSelectBtn = rootView.findViewById();

        calendarTv = rootView.findViewById(R.id.profile_fragment_edit_details_calendar_text_view);
        calendarBtn = rootView.findViewById(R.id.profile_fragment_edit_details_calendar_layout);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dialog;

                dialog = new DatePickerDialog(getActivity(),R.style.MySpinnerDatePickerStyle ,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                        String sMonth = "" + mMonth;
                        if(mMonth < 10){
                            sMonth = "0" + mMonth;
                        }

                        String sDay = "" + mDay;
                        if(mDay < 10){
                            sDay = "0" + sDay;
                        }
                        birthday = "" + mYear +"/" + sMonth + "/" + sDay;
                        calendarTv.setText(birthday);

                    }
                }, year, month, day);
                // dialog.updateDate(year,month,day);


                dialog.show();
            }

        });



        aboutMeEt = rootView.findViewById(R.id.profile_fragment_edit_details_about_me_et);
        addressEt = rootView.findViewById(R.id.profile_fragment_edit_details_address_et);

        arabic = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_arabic);
        chinese = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_chinese);
        english = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_english);
        french = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_french);
        german = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_german);
        hebrew = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_hebrew);
        italian = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_italian);
        japanese = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_japanese);
        korean = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_korean);
        russian = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_russian);







        ///////////////////////////////////////


        return rootView;
    }

    public SignUpFragment2(BottomNavigationView bottomNavigationView, ViewPager pager) {

        this.navigationView = bottomNavigationView;
        this.pager = pager;
    }

    public SignUpFragment2(BottomNavigationView bottomNavigationView, ViewPager pager,MainActivity mainActivity) {

        this.navigationView = bottomNavigationView;
        this.pager = pager;
        this.mainActivity = mainActivity;

    }

    public class SkipUpdateBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            getActivity().getSupportFragmentManager().popBackStack();
            navigationView.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);

            mainActivity.initPager();
        }
    }

    public class UpdateProfileBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {


            String monthStr = "";
            String dayStr = "";

   /*         int year = birth_day_picker.getYear();
            int month = birth_day_picker.getMonth() + 1;
            int day = birth_day_picker.getDayOfMonth();

            if (month < 10)
            {
                monthStr = "0" + month;
            }
            if (day < 10)
            {
                dayStr = "0" + day;
            }*/
           // birthday = year + "/" + monthStr + "/" + dayStr;
            Log.d("myBirthday", birthday);

            String about_me = aboutMeEt.getText().toString();

            DatabaseManager.updateUserData(FirebaseManager.getCurrentUserId(),about_me,null,birthday,null,0,0);
            mainActivity.initPager();
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

    private Map<String, Boolean> setLangueges() {

        Map<String, Boolean> langeuges = new HashMap<>();
        langeuges.put(arabic.getText().toString(), arabic.isChecked());
        langeuges.put(chinese.getText().toString(), chinese.isChecked());
        langeuges.put(english.getText().toString(), english.isChecked());
        langeuges.put(french.getText().toString(), french.isChecked());
        langeuges.put(german.getText().toString(), german.isChecked());
        langeuges.put(hebrew.getText().toString(), hebrew.isChecked());
        langeuges.put(italian.getText().toString(), italian.isChecked());
        langeuges.put(japanese.getText().toString(), japanese.isChecked());
        langeuges.put(korean.getText().toString(), korean.isChecked());
        langeuges.put(russian.getText().toString(), russian.isChecked());


        return langeuges;
    }
}
