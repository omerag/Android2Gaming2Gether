package hit.android2.Database;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hit.android2.R;

public class FirebaseManager {


    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();

    //UserState
    private FirebaseAuth.AuthStateListener authStateListener = new AuthStateChangeListener();
    private String userName;
    private NavigationView navigationView;
    private TextView userTv;



    ////////////////////////////////////////////////////////////////////////////////////////

    public FirebaseAuth.AuthStateListener getAuthStateListener() {
        return authStateListener;
    }

    public FirebaseAuth getFireBaseAuth() {
        return fireBaseAuth;
    }

    public void signUpUser(final String username, final String email, final String password){

        userName = username;
        fireBaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

     /*           logOutUser();
                logInUser(email,password);*/

                UserData user = new UserData(username,fireBaseAuth.getCurrentUser().getUid());
                DatabaseManager.addUserToDatabase(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FirebaseManager",e.getMessage());
            }
        });

    }

    public void logInUser(String email, String password){

        fireBaseAuth.signInWithEmailAndPassword(email, password);
    }

    public void logOutUser(){

        fireBaseAuth.signOut();
    }


    class AuthStateChangeListener implements FirebaseAuth.AuthStateListener
    {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            FirebaseUser currUser = firebaseAuth.getCurrentUser();

            if (currUser != null)
            {
                if (userName != null)
                {
                    currUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            userName = null;
                        }
                    });
                }

                userTv.setText(currUser.getDisplayName());
                navigationView.getMenu().findItem(R.id.sign_up).setVisible(false);
                navigationView.getMenu().findItem(R.id.log_in).setVisible(false);
                navigationView.getMenu().findItem(R.id.log_out).setVisible(true);
            }
            else {
                userTv.setText(R.string.user_name_tv);
                navigationView.getMenu().findItem(R.id.sign_up).setVisible(true);
                navigationView.getMenu().findItem(R.id.log_in).setVisible(true);
                navigationView.getMenu().findItem(R.id.log_out).setVisible(false);
            }

        }
    }

    public void setReference(NavigationView navigation, TextView userName)
    {
        navigationView = navigation;
        userTv = userName;
    }

    public void registerAuthListener(){
        fireBaseAuth.addAuthStateListener(authStateListener);
    }

    public void unRegisterAuthListener(){
        fireBaseAuth.removeAuthStateListener(authStateListener);
    }





}
