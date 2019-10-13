package hit.android2.Database;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.List;

import hit.android2.Database.Model.ChildData;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.ParentData;
import hit.android2.Database.Model.UserData;

public class DatabaseManager {



    //need to add adapter to notify after updates
    static public void getGameFromDatabase(final String gameGuid, final List<GameData> gameDataList, final RecyclerView.Adapter adapter) {
        Log.d("DatabaseManager","getGameFromDatabase called");

        FirebaseFirestore.getInstance().collection("games").document(gameGuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","getGameFromDatabase onSuccess called");

                if(documentSnapshot.exists()){
                    Log.d("DatabaseManager","getGameFromDatabase documentSnapshot is exists");


                    GameData gameData = documentSnapshot.toObject(GameData.class);
                    gameDataList.add(gameData);
                    adapter.notifyItemInserted(gameDataList.size()-1);
                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }


    static public void loadGameIntoViews(final String gameGuid, final TextView gameName, final ImageView gameImage, final Context context) {
        Log.d("DatabaseManager","loadGameIntoViews called");

        FirebaseFirestore.getInstance().collection("games").document(gameGuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","loadGameIntoViews onSuccess called");

                if(documentSnapshot.exists()){
                    Log.d("DatabaseManager","loadGameIntoViews documentSnapshot is exists");

                    GameData gameData = documentSnapshot.toObject(GameData.class);
                    gameName.setText(gameData.getName());
                    Glide.with(context).load(gameData.getImageUrl()).into(gameImage);

                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }



    static public void getHomeTopics(String userId, final List<ParentData> topics, final RecyclerView.Adapter adapter){
        Log.d("DatabaseManager","getUserGamesGUID called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","getUserGamesGUID onSuccess called");

                if(documentSnapshot.exists()){
                    UserData user = documentSnapshot.toObject(UserData.class);
                    List<String> games = user.getGames();

                    for(String game : games){
                        DatabaseManager.getTopicsByGame(game,topics,adapter);
                    }


                    //games.addAll(user.getGames());
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager",e.getMessage());
            }
        });


    }

    static public void getUserFromDatabase(String userId, final List<UserData> users, final RecyclerView.Adapter adapter){
        Log.d("DatabaseManager","getUserFromDatabase called");

        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","getUserFromDatabase onSuccess called");

                if(documentSnapshot.exists()){
                    Log.d("DatabaseManager","getUserFromDatabase documentSnapshot is exists");


                    UserData user = documentSnapshot.toObject(UserData.class);
                    users.add(user);
                    adapter.notifyItemInserted(users.size()-1);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }

    static public void getUserFromDatabase(final String userId, final UserData friend, final TextView friendName, final ImageView imageView, final Context context){
        Log.d("DatabaseManager","getUserFromDatabase called");

        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","getUserFromDatabase onSuccess called");

                if(documentSnapshot.exists()){
                    Log.d("DatabaseManager","getUserFromDatabase documentSnapshot is exists");

                    UserData user = documentSnapshot.toObject(UserData.class);

                    if(friend != null){
                        friend.setName(user.getName());
                        friend.setImageUrl(user.getImageUrl());
                        friend.setKey(user.getKey());
                    }


                    friendName.setText(user.getName());

                    Glide.with(context).load(user.getImageUrl()).into(imageView);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }

    static public void addGameToDatabase(final GameData game) {
        Log.d("DatabaseManager","addGameToDatabase called");

        final DocumentReference gameRef = FirebaseFirestore.getInstance().collection("games").document(game.getGuid());
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "game already exists in database");

                    gameRef.update("users",FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                } else {
                    List<String> users = game.getUsers();
                    users.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        System.out.println("user id = " + user.getKey());
        FirebaseFirestore.getInstance().collection("users").document(user.getKey()).set(user);
    }


    //need to add adapter to notify after updates
    //search for users by game guid
    //fill the list players with user
    static public void searchPlayers(final String gameGuid, final List<UserData> players, final RecyclerView.Adapter adapter) {
        Log.d("DatabaseManager", "searchPlayers called\nsearching for players , game = " + gameGuid);


        FirebaseFirestore.getInstance().collection("users").whereArrayContains("games",gameGuid).orderBy("totalRank", Query.Direction.DESCENDING)
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

    static public void getUserGames(String userId, final List<GameData> games, final RecyclerView.Adapter adapter){

        Log.d("DatabaseManager","getUserGames called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","getUserGames OnSuccess called");

                UserData user = documentSnapshot.toObject(UserData.class);
                List<String> gameKeys = user.getGames();

                for(String gameKey : gameKeys){
                    getGameFromDatabase(gameKey,games,adapter);
                }

               // adapter.notifyDataSetChanged();
            }
        });
    }

    static public void getUserFriends(final String userId, final List<UserData> users, final RecyclerView.Adapter adapter){
        Log.d("DatabaseManager","getUserFriends called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","getUserFriends OnSuccess called");

                UserData user = documentSnapshot.toObject(UserData.class);
                List<String> freinds = user.getFriends();

                for(String friend : freinds){
                    getUserFromDatabase(friend,users,adapter);
                }

                // adapter.notifyDataSetChanged();
            }
        });
    }

    static public void getUsersFromList(List<String> userId, final List<UserData> users){
        Log.d("DatabaseManager", "getUsersFromList called");

        FirebaseFirestore.getInstance().collection("users").whereArrayContains("key",userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("DatabaseManager", "getUsersFromList onSuccess called");

                for(QueryDocumentSnapshot userDocument : queryDocumentSnapshots){
                    UserData user = userDocument.toObject(UserData.class);

                    users.add(user);
                }
                
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());

            }
        });

    }

    static public void getTopicsByGame(String guid, final List<ParentData> topics, final RecyclerView.Adapter adapter){
        Log.d("DatabaseManager","getTopicsByGame called");

        CollectionReference topicsReff = FirebaseFirestore.getInstance().collection("games").document(guid).collection("topics");

        topicsReff.orderBy("timestamp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("DatabaseManager","getTopicsByGame onSuccess called");

                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                    ParentData topic = document.toObject(ParentData.class);
                    topic.setId(document.getId());

                    Log.d("DatabaseManager","topic name = " + topic.getTitle());

                    topics.add(topic);
                }

                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager",e.getMessage());
            }
        });

    }


    static public void addTopicToDatabase(String guid, ParentData topic){
        Log.d("DatabaseManager","addTopicToDatabase called");

        FirebaseFirestore.getInstance().collection("games")
                .document(guid).collection("topics").add(topic);

    }

    static public void updateTopic(String guid, String topicID , final List<ChildData> comments){
        Log.d("DatabaseManager","UpdateTopic called");

        final DocumentReference topicReff = FirebaseFirestore.getInstance().collection("games")
                .document(guid).collection("topics").document(topicID);

        topicReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager","UpdateTopic onSuccess called");

                if(documentSnapshot.exists()){
                    topicReff.update("items",comments);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager",e.getMessage());

            }
        });


    }




}
