package hit.android2.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class DatabaseManager {

    //need to add adapter to notify after updates
    static public void getGameFromDatabase(String gameGuid, final List<GameData> gameDataList, final RecyclerView.Adapter adapter) {
        FirebaseFirestore.getInstance().document(gameGuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                GameData gameData = documentSnapshot.toObject(GameData.class);
                gameDataList.add(gameData);
                adapter.notifyItemInserted(gameDataList.size()-1);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }

    static public void addGameToDatabase(final GameData game) {

        final DocumentReference gameRef = FirebaseFirestore.getInstance().collection("games").document(game.getGuid());
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "game already exists in database");
                } else {
                    gameRef.set(game);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Database", e.getMessage());
            }
        });
    }

    static public void addUserToDatabase(final UserData user){
        System.out.println("ERROR - user id = " + user.getKey());
        FirebaseFirestore.getInstance().collection("users").document(user.getKey()).set(user);
    }


    //need to add adapter to notify after updates
    //search for users by game guid
    //fill the list players with user
    static public void searchPlayers(final String gameGuid, final List<UserData> players, final RecyclerView.Adapter adapter) {
        FirebaseFirestore.getInstance().collection("users").whereArrayContains("games",gameGuid).orderBy("totalRank")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot playerDocument : queryDocumentSnapshots) {
                            UserData player = playerDocument.toObject(UserData.class);
                            players.add(player);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseManager", e.getMessage());
                    }
                });
    }

    static public void userAddFriend(String userId, final String friendID) {
        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
       /*         UserData user = documentSnapshot.toObject(UserData.class);

                List<String> friends = user.getFriends();
                friends.add(friendID);*/

                userRef.update("friends", FieldValue.arrayUnion(friendID));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager",e.getMessage());
            }
        });
    }

   static public void userAddGame(String userId, final String game){

        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                System.out.println("userAddGame - onSuccess game name = " + game);

              /*  UserData user = documentSnapshot.toObject(UserData.class);

                List<String> games = user.getGames();
                games.add(game);*/

                userRef.update("games",FieldValue.arrayUnion("games",game));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager",e.getMessage());
            }
        });

    }


}
