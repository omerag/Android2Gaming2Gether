package hit.android2;

import com.google.firebase.auth.FirebaseAuth;

public class FireBaseManager {

    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener = new MainActivity.AuthStateChangedListener();

    public FirebaseAuth.AuthStateListener getAuthStateListener() {
        return authStateListener;
    }

    public FirebaseAuth getFireBaseAuth() {
        return fireBaseAuth;
    }

    public void signUpUser(String username, String email, String password){

        fireBaseAuth.createUserWithEmailAndPassword(email,password);

    }

    public void logInUser(String email, String password){

        fireBaseAuth.signInWithEmailAndPassword(email, password);
    }

    public void logOutUser(){

        fireBaseAuth.signOut();
    }
}
