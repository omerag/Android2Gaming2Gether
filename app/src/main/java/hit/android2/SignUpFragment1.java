package hit.android2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Managers.MessegingManager;

public class SignUpFragment1 extends Fragment {

    private Button sign_up_btn;
    private ImageButton back_btn;
    private TextInputEditText userNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private BottomNavigationView navigationView;
    private ViewPager pager;

    private FirebaseManager firebaseManager;

    private MainActivity mainActivity;
    private ProgressDialog progressDialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_fragment1, container, false);

        firebaseManager = new FirebaseManager();

        sign_up_btn = rootView.findViewById(R.id.sign_up_btn);
        back_btn = rootView.findViewById(R.id.back_btn);

        userNameInput = rootView.findViewById(R.id.sign_up_username_et);
        emailInput = rootView.findViewById(R.id.sign_up_email_et);
        passwordInput = rootView.findViewById(R.id.sign_up_password_et);

        SignUpBtnListener signUpBtnListener = new SignUpBtnListener();
        sign_up_btn.setOnClickListener(signUpBtnListener);

        BackBtnListener backBtnListener = new BackBtnListener();
        back_btn.setOnClickListener(backBtnListener);

        return rootView;
    }

    public SignUpFragment1(BottomNavigationView bottomNavigationView, ViewPager pager) {

        this.navigationView = bottomNavigationView;
        this.pager = pager;
    }

    public SignUpFragment1(BottomNavigationView bottomNavigationView, ViewPager pager,MainActivity mainActivity) {

        this.navigationView = bottomNavigationView;
        this.pager = pager;
        this.mainActivity = mainActivity;
    }

    public class SignUpBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.progress_message));
            progressDialog.setTitle(getString(R.string.progress_title_2));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            String userName = userNameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            firebaseManager.signUpUser(userName, email, password, new DatabaseManager.Listener() {
                @Override
                public void onSuccess() {
                    getActivity().getSupportFragmentManager().popBackStack();

                    MessegingManager.subscribeToTopic(FirebaseManager.getCurrentUserId());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.dialog_fragments_container, new SignUpFragment2(navigationView, pager,mainActivity))
                            .addToBackStack("myFragment").commit();
                    progressDialog.dismiss();

                }
            });

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
