package hit.android2.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DatabaseManager {

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    public DatabaseManager(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }


    //need to add adapter to notify after updates
    public void getGameFromDatabase(String gameGuid, final List<GameData> gameDataList, final RecyclerView.Adapter adapter) {
        database.document(gameGuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    public void addGameToDatabase(final GameData game) {

        final DocumentReference gameRef = database.collection("games").document(game.getGameId());
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


    //need to add adapter to notify after updates
    //search for users by game guid
    //fill the list players with user
    public void searchPlayers(final String gameGuid, final List<UserData> players, final RecyclerView.Adapter adapter) {
        database.collection("users").whereArrayContains("games",gameGuid).orderBy("totalRank")
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

    public void userAddFriend(String userId, final String friendID) {
        final DocumentReference userRef = database.collection("users").document(userId);
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

    public void userAddGame(String userId, final String game){

        final DocumentReference userRef = database.collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData user = documentSnapshot.toObject(UserData.class);

                List<String> games = user.getGames();
                games.add(game);

                userRef.update("games",games);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager",e.getMessage());
            }
        });

    }


}
