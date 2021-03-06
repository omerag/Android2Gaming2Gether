package hit.android2.Database.Managers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Database.Model.UserData;

import hit.android2.R;

public class FirebaseManager {

    private Listener listener;

    public interface Listener{

        void onSuccess();
    }


    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();

    //UserState
    public FirebaseAuth.AuthStateListener authStateListener = new AuthStateChangeListener();
    private String userName;
    private NavigationView navigationView;
    private TextView userTv;
    private String lastMessage;
    private Context myContext;
    private CircleImageView profile_pic;
    private UserData currentUserData;

    public FirebaseManager() {
        loadCurrentUserData();
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    public FirebaseAuth.AuthStateListener getAuthStateListener() {
        return authStateListener;
    }

    public FirebaseAuth getFireBaseAuth() {
        return fireBaseAuth;
    }

    public void signUpUser(final String username, final String email, final String password, final DatabaseManager.Listener listener){

        userName = username;
        fireBaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                FirebaseUser firebaseUser = fireBaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    UserData user = new UserData(username,fireBaseAuth.getCurrentUser().getUid());
                    DatabaseManager.addUserToDatabase(user, listener);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                
                Log.d("FirebaseManager",e.getMessage());
            }
        });

    }

    public void logInUser(String email, String password, Listener listener){

        fireBaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                listener.onSuccess();




            }
        });


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

                loadUserPicture();
                userTv.setText(currUser.getDisplayName());
                navigationView.getMenu().findItem(R.id.sign_up).setVisible(false);
                navigationView.getMenu().findItem(R.id.log_in).setVisible(false);
                navigationView.getMenu().findItem(R.id.log_out).setVisible(true);
            }
            else {
                profile_pic.setImageResource(R.drawable.blank_profile_img);
                userTv.setText(R.string.user_name_tv);
                navigationView.getMenu().findItem(R.id.sign_up).setVisible(true);
                navigationView.getMenu().findItem(R.id.log_in).setVisible(true);
                navigationView.getMenu().findItem(R.id.log_out).setVisible(false);
            }

        }
    }


    public void setReference(NavigationView navigation, TextView userName,Context context, CircleImageView profile_image)
    {
        navigationView = navigation;
        userTv = userName;
        myContext = context;
        profile_pic = profile_image;
    }

    public void registerAuthListener(){
        fireBaseAuth.addAuthStateListener(authStateListener);
    }

    public void unRegisterAuthListener(){
        fireBaseAuth.removeAuthStateListener(authStateListener);
    }

    public static boolean isLoged(){

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            return true;
        }

        return false;
    }

    public void sendMessage(final String sender, final String receiver, final String message, final Context context, final String userName){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        databaseReference.child("Chats").child(sender).child(receiver).push().setValue(hashMap);
        databaseReference.child("Chats").child(receiver).child(sender).push().setValue(hashMap)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                MessegingManager.notifyNewMessegInChat(context,userName,receiver,message);
            }
        });


        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child("userChatList")
                .child(sender)
                .child(receiver);


        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefFriend = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child("userChatList")
                .child(receiver)
                .child(sender);


        chatRefFriend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    chatRefFriend.child("id").setValue(sender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void sendGroupMessage(String groupId, String senderId, String senderName, String msg, Listener listener)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", senderId);
        hashMap.put("senderName", senderName);
        hashMap.put("message", msg);

        databaseReference.child("GroupChats").child(groupId).push().setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess();
                    }
                });
    }


    public void GetLastMessage(final String userid, final TextView lastMessageTv)
    {
        lastMessage = "default";

        final FirebaseUser firebaseUser = getFireBaseAuth().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        Query lastQuery = reference.child(firebaseUser.getUid()).child(userid).orderByKey().limitToLast(1);

        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    lastMessage = snapshot.child("message").getValue().toString();

                    if (lastMessage.equals("default"))
                    {
                        lastMessageTv.setVisibility(View.INVISIBLE);
                    }
                    else {
                        lastMessageTv.setText(lastMessage);
                        lastMessage = "default";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void GetLastGroupMessage(final String groupId, final TextView lastMessageTv)
    {
        lastMessage = "default";

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChats");
        Query lastQuery = reference.child(groupId).orderByKey().limitToLast(1);

        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    lastMessage = snapshot.child("message").getValue().toString();

                    if (lastMessage.equals("default"))
                    {
                        lastMessageTv.setVisibility(View.INVISIBLE);
                    }
                    else {
                        String sender_name = snapshot.child("senderName").getValue().toString();
                        String lastMessageDisplay = sender_name + " : " + lastMessage;
                        lastMessageTv.setText(lastMessageDisplay);
                        lastMessage = "default";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadCurrentUserData(){

        if(isLoged()){
            DatabaseManager.getUserFromDatabase(getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
                @Override
                public void onSuccess(UserData userData) {
                    currentUserData = userData;
                }
            });
        }
    }

    public UserData getCurrentUserData() {
        return currentUserData;
    }

    static public String getCurrentUserId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void loadUserPicture()
    {

            DatabaseManager.getUserFromDatabase(getFireBaseAuth().getCurrentUser().getUid(), new DatabaseManager.DataListener<UserData>() {
                @Override
                public void onSuccess(UserData userData) {

                    if (userData.getImageUrl() == null)
                    {
                        profile_pic.setImageResource(R.drawable.blank_profile_img);
                    }
                    else {

                        Glide.with(myContext).load(userData.getImageUrl()).into(profile_pic);
                    }
                }
            });
        }
}
