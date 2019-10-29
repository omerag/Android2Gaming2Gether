package hit.android2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.time.Month;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.UserData;
import hit.android2.Helpers.GeoHelper;

public class ProfileEditDetailsFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;
    private MenuItem saveEditBtn;

    private ImageButton back_btn;
    private Button saveBtn;

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
        final View rootView = inflater.inflate(R.layout.profile_dialog_edit_details_fragment, container, false);

        back_btn = rootView.findViewById(R.id.back_btn);

        BackBtnListener backBtnListener = new BackBtnListener();
        back_btn.setOnClickListener(backBtnListener);

        saveBtn = rootView.findViewById(R.id.profile_fragment_edit_details_save_changes_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
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
        arabic.setTag("Arabic");
        chinese = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_chinese);
        chinese.setTag("Chinese");
        english = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_english);
        english.setTag("English");
        french = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_french);
        french.setTag("French");
        german = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_german);
        german.setTag("German");
        hebrew = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_hebrew);
        hebrew.setTag("Hebrew");
        italian = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_italian);
        italian.setTag("Italian");
        japanese = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_japanese);
        japanese.setTag("Japanese");
        korean = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_korean);
        korean.setTag("Korean");
        russian = rootView.findViewById(R.id.profile_fragment_edit_details_checkbox_russian);
        rootView.setTag("Russian");


        initDetails();
        return rootView;
    }

    public ProfileEditDetailsFragment(BottomNavigationView bottomNavigationView, ViewPager pager) {

        this.bottomNavigationView = bottomNavigationView;
        this.pager = pager;
    }

    class BackBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();

            bottomNavigationView.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
        }
    }

    private Map<String, Boolean> setLangueges() {

        Map<String, Boolean> langeuges = new HashMap<>();
        langeuges.put(arabic.getTag().toString(), arabic.isChecked());
        langeuges.put(chinese.getTag().toString(), chinese.isChecked());
        langeuges.put(english.getTag().toString(), english.isChecked());
        langeuges.put(french.getTag().toString(), french.isChecked());
        langeuges.put(german.getTag().toString(), german.isChecked());
        langeuges.put(hebrew.getTag().toString(), hebrew.isChecked());
        langeuges.put(italian.getTag().toString(), italian.isChecked());
        langeuges.put(japanese.getTag().toString(), japanese.isChecked());
        langeuges.put(korean.getTag().toString(), korean.isChecked());
        langeuges.put(russian.getTag().toString(), russian.isChecked());


        return langeuges;
    }

    private void initDetails() {



        DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
            @Override
            public void onSuccess(UserData userData) {



                Map<String, Boolean> langMap = userData.getLanguage();

                arabic.setChecked(langMap.get("Arabic"));
                chinese.setChecked(langMap.get("Chinese"));
                english.setChecked(langMap.get("English"));
                french.setChecked(langMap.get("French"));
                german.setChecked(langMap.get("German"));
                hebrew.setChecked(langMap.get("Hebrew"));
                italian.setChecked(langMap.get("Italian"));
                japanese.setChecked(langMap.get("Japanese"));
                korean.setChecked(langMap.get("Korean"));
                russian.setChecked(langMap.get("Russian"));

                aboutMeEt.setText(userData.getAboutMe(), EditText.BufferType.EDITABLE);

                calendarTv.setText(userData.getBirthday_timestamp());

                gender = userData.getGender();

                if(gender.equals("male")){
                    genderSelectBtn = getView().findViewById(R.id.profile_fragment_edit_details_radio_male);
                    genderSelectBtn.setChecked(true);
                }
                else if(gender.equals("female")){
                    genderSelectBtn = getView().findViewById(R.id.profile_fragment_edit_details_radio_female);
                    genderSelectBtn.setChecked(true);
                }




            }
        });

    }
}
