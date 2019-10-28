package hit.android2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import hit.android2.Database.Managers.FirebaseManager;

public class LoginFragment extends Fragment {

    private BottomNavigationView navigationView;
    private ViewPager pager;
    private Button login_btn;
    private ImageButton back_btn;


    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    private FirebaseManager firebaseManager;

    private MainActivity mainActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        firebaseManager = new FirebaseManager();

        emailInput = rootView.findViewById(R.id.sign_up_email_et);
        passwordInput = rootView.findViewById(R.id.sign_up_password_et);
        login_btn = rootView.findViewById(R.id.login_btn);
        back_btn = rootView.findViewById(R.id.back_btn);


        LoginBtnListener loginBtnListener = new LoginBtnListener();
        login_btn.setOnClickListener(loginBtnListener);

        BackBtnListener backBtnListener = new BackBtnListener();
        back_btn.setOnClickListener(backBtnListener);


        return rootView;
    }

    public LoginFragment(BottomNavigationView bottomNavigationView, ViewPager pager) {

        this.navigationView = bottomNavigationView;
        this.pager = pager;
    }

    public LoginFragment(BottomNavigationView bottomNavigationView, ViewPager pager, MainActivity mainActivity) {

        this.navigationView = bottomNavigationView;
        this.pager = pager;
        this.mainActivity = mainActivity;
    }


    public class LoginBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            firebaseManager.logInUser(email, password, new FirebaseManager.Listener() {
                @Override
                public void onSuccess() {
                    mainActivity.initPager();
                }
            });
            getActivity().getSupportFragmentManager().popBackStack();
            navigationView.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
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
