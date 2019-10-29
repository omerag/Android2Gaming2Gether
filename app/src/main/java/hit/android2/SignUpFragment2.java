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
import com.google.android.material.snackbar.Snackbar;

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

        aboutMeEt = rootView.findViewById(R.id.fragment_sign_up2_edit_details_about_me_et);



        ///////////////////////////////////////


        update_profile_btn = rootView.findViewById(R.id.fragment_sign_up2_update_profile_btn);
        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                genderSelectBtn = rootView.findViewById(genderSelectGroup.getCheckedRadioButtonId());
                if (genderSelectBtn.getId() == R.id.fragment_sign_up2_edit_details_radio_male) {
                    gender = "male";
                } else if (genderSelectBtn.getId() == R.id.fragment_sign_up2_edit_details_radio_female) {
                    gender = "female";
                } else {
                    gender = "all";
                }


                if(addressEt.getText().toString().equals("")){
                    DatabaseManager.updateUserData(FirebaseManager.getCurrentUserId(), aboutMeEt.getText().toString(), setLangueges(), birthday, gender, 0, 0);
                    Snackbar.make(getView(),getString(R.string.saved),3000).show();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(SignUpFragment2.this).commit();
                    mainActivity.initPager();
                    mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                    pager.setVisibility(View.VISIBLE);

                }
                else {
                    new GeoHelper(getActivity(), null, addressEt.getText().toString(), new GeoHelper.Listener<Double>() {
                        @Override
                        public void onSuccess(Double latitude, Double longitude) {
                            DatabaseManager.updateUserData(FirebaseManager.getCurrentUserId(), aboutMeEt.getText().toString(), setLangueges(), birthday, gender, latitude, longitude);
                            Snackbar.make(getView(),getString(R.string.saved),3000).show();
                            getActivity().getSupportFragmentManager().beginTransaction().remove(SignUpFragment2.this).commit();
                            mainActivity.initPager();
                            mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                            pager.setVisibility(View.VISIBLE);

                        }
                    });
                }

            }
        });

        genderSelectGroup = rootView.findViewById(R.id.fragment_sign_up2_edit_details_radio_group);
        //genderSelectBtn = rootView.findViewById();

        calendarTv = rootView.findViewById(R.id.fragment_sign_up2_edit_details_calendar_text_view);
        calendarBtn = rootView.findViewById(R.id.fragment_sign_up2_edit_details_calendar_layout);
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



        aboutMeEt = rootView.findViewById(R.id.fragment_sign_up2_edit_details_about_me_et);
        addressEt = rootView.findViewById(R.id.fragment_sign_up2_dit_details_address_et);

        arabic = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_arabic);
        arabic.setTag("Arabic");
        chinese = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_chinese);
        chinese.setTag("Chinese");

        english = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_english);
        english.setTag("English");

        french = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_french);
        french.setTag("French");

        german = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_german);
        german.setTag("German");

        hebrew = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_hebrew);
        hebrew.setTag("Hebrew");

        italian = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_italian);
        italian.setTag("Italian");

        japanese = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_japanese);
        japanese.setTag("Japanese");

        korean = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_korean);
        korean.setTag("Korean");

        russian = rootView.findViewById(R.id.fragment_sign_up2_edit_details_checkbox_russian);
        rootView.setTag("Russian");





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
}
