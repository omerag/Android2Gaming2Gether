package hit.android2.Database.Managers;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import hit.android2.Database.Model.ChildData;
import hit.android2.Database.Model.GameData;
import hit.android2.Database.Model.GroupData;
import hit.android2.Database.Model.ParentData;
import hit.android2.Database.Model.UserData;
import hit.android2.Model.Chatlist;

public class DatabaseManager {

    public interface Listener {
        void onSuccess();
    }

    public interface UserDataListener {
        void onSuccess(UserData user);

    }

    public interface DataListener<T> {

        void onSuccess(T t);
    }


    static public void getGameFromDatabase(final String gameGuid, final List<GameData> gameDataList, final RecyclerView.Adapter adapter) {
        Log.d("DatabaseManager", "getGameFromDatabase called");

        FirebaseFirestore.getInstance().collection("games").document(gameGuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getGameFromDatabase onSuccess called");

                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "getGameFromDatabase documentSnapshot is exists");


                    GameData gameData = documentSnapshot.toObject(GameData.class);
                    gameDataList.add(gameData);
                    adapter.notifyDataSetChanged();

                    System.out.println("games.size() = " + gameDataList.size());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }

    static public void getGameFromDatabase(final String gameGuid, final DatabaseManager.DataListener<GameData> listener) {
        Log.d("DatabaseManager", "getGameFromDatabase called");

        FirebaseFirestore.getInstance().collection("games").document(gameGuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getGameFromDatabase onSuccess called");

                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "getGameFromDatabase documentSnapshot is exists");


                    GameData gameData = documentSnapshot.toObject(GameData.class);
                    listener.onSuccess(gameData);
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
        Log.d("DatabaseManager", "loadGameIntoViews called");

        FirebaseFirestore.getInstance().collection("games").document(gameGuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "loadGameIntoViews onSuccess called");

                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "loadGameIntoViews documentSnapshot is exists");

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

    static public void getHomeTopics(String userId, final List<ParentData> topics, final Listener listener) {
        Log.d("DatabaseManager", "getUserGamesGUID called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserGamesGUID onSuccess called");

                if (documentSnapshot.exists()) {
                    UserData user = documentSnapshot.toObject(UserData.class);
                    List<String> games = user.getGames();

                    for (String game : games) {
                        DatabaseManager.getTopicsByGame(game, topics, listener);
                    }


                    //games.addAll(user.getGames());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());
            }
        });


    }

    static public void getUserFromDatabase(String userId, final List<UserData> users, final Listener listener) {
        Log.d("DatabaseManager", "getUserFromDatabase called");

        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserFromDatabase onSuccess called");

                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "getUserFromDatabase documentSnapshot is exists");


                    UserData user = documentSnapshot.toObject(UserData.class);
                    users.add(user);
                }

                listener.onSuccess();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }

    static public void getUserFromDatabase(final String userId, final UserData friend, final TextView friendName, final ImageView imageView, final Context context) {
        Log.d("DatabaseManager", "getUserFromDatabase called");

        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserFromDatabase onSuccess called");

                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "getUserFromDatabase documentSnapshot is exists");

                    UserData user = documentSnapshot.toObject(UserData.class);

                    if (friend != null) {
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

    static public void getUserFromDatabase(final String userId, final UserData friend, final TextView friendName, final ImageView imageView, final Context context, final UserDataListener listener) {
        Log.d("DatabaseManager", "getUserFromDatabase called");

        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserFromDatabase onSuccess called");

                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "getUserFromDatabase documentSnapshot is exists");

                    UserData user = documentSnapshot.toObject(UserData.class);

                    if (friend != null) {
                        friend.setName(user.getName());
                        friend.setImageUrl(user.getImageUrl());
                        friend.setKey(user.getKey());
                    }


                    friendName.setText(user.getName());

                    Glide.with(context).load(user.getImageUrl()).into(imageView);

                    System.out.println("**********************8\naboutme = " + user.getAboutMe() + "\n**************");
                    listener.onSuccess(user);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }

    static public void getUserFromDatabase(String userId, final DataListener<UserData> listener) {
        Log.d("DatabaseManager", "getUserFromDatabase called");

        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserFromDatabase onSuccess called");

                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "getUserFromDatabase documentSnapshot is exists");


                    UserData user = documentSnapshot.toObject(UserData.class);
                    listener.onSuccess(user);

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
        Log.d("DatabaseManager", "addGameToDatabase called");

        final DocumentReference gameRef = FirebaseFirestore.getInstance().collection("games").document(game.getGuid());
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("DatabaseManager", "game already exists in database");

                    gameRef.update("users", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()));
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

    static public void addUserToDatabase(final UserData user) {
        System.out.println("user id = " + user.getKey());
        FirebaseFirestore.getInstance().collection("users").document(user.getKey()).set(user);
    }

    static public void addUserToDatabase(final UserData user, final Listener listener) {
        System.out.println("user id = " + user.getKey());
        FirebaseFirestore.getInstance().collection("users").document(user.getKey()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess();
                    }
                });
    }

    static public void searchPlayers(final String gameGuid, final List<UserData> players, final RecyclerView.Adapter adapter) {
        Log.d("DatabaseManager", "searchPlayers called\nsearching for players , game = " + gameGuid);


        FirebaseFirestore.getInstance().collection("users").whereArrayContains("games", gameGuid).orderBy("totalRank", Query.Direction.DESCENDING)
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

    static public void searchPlayers(final UserData mUser, final String gameGuid, String language, String gender, String rankType, int maxAge, final float maxDistance, final DataListener<List<UserData>> listener) {
        Log.d("DatabaseManager", "searchPlayers called\nsearching for players , game = " + gameGuid);

        //////////
        Calendar calendar = Calendar.getInstance();
        Date now = new Date(); //init to current date
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year - maxAge + 1, month, day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        final String maxBirthday = format.format(calendar.getTime());
        Log.d("searchPlayers", "MaxAge = " + maxBirthday);
        ///////////
        Log.d("searchPlayers", "rankType = " + rankType);
        Log.d("searchPlayers", "language = " + language);
        Log.d("searchPlayers", "gender = " + gender);
        Log.d("searchPlayers", "maxDistance = " + maxDistance);
        Log.d("searchPlayers", "mUser.getName() = " + mUser.getName());


        CollectionReference usersReff = FirebaseFirestore.getInstance().collection("users");
        Query query;

        if (gender.equals("all")) {
            query = usersReff.whereArrayContains("games", gameGuid)
                    .whereEqualTo(language, true)
                    .whereGreaterThanOrEqualTo(rankType, 0)
                    // .whereGreaterThan("birthday_timestamp", birthdayTimestamp)
                    .orderBy(rankType, Query.Direction.DESCENDING)
            // .orderBy("birthday_timestamp", Query.Direction.DESCENDING)
            ;
        } else {
            query = usersReff.whereArrayContains("games", gameGuid)
                    .whereEqualTo(language, true)
                    .whereEqualTo("gender", gender)
                    .whereGreaterThanOrEqualTo(rankType, 0)
                    //.whereGreaterThan("birthday_timestamp", birthdayTimestamp)
                    .orderBy(rankType, Query.Direction.DESCENDING)
            // .orderBy("birthday_timestamp", Query.Direction.DESCENDING);
            ;
        }


        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("DatabaseManager", "searchPlayers called - onSuccess");
                Log.d("DatabaseManager", "queryDocumentSnapshots.size() = " + queryDocumentSnapshots.size());

                List<UserData> players = new ArrayList<>();
                for (QueryDocumentSnapshot playerDocument : queryDocumentSnapshots) {
                    UserData player = playerDocument.toObject(UserData.class);
                    players.add(player);
                    Log.d("DatabaseManager", "searchPlayers called - onSuccess inside For loop\n" +
                            "player = " + player.getName());


                }

                players = filerPlayerList(mUser, players, maxBirthday, maxDistance);
                listener.onSuccess(players);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());
            }
        });
    }

    static public void userAddFriend(String userId, final String friendID, final Listener listener) {
        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
       /*         UserData user = documentSnapshot.toObject(UserData.class);

                List<String> friends = user.getFriends();
                friends.add(friendID);*/

                userRef.update("friends", FieldValue.arrayUnion(friendID))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listener.onSuccess();
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());
            }
        });
    }

    static public void userAddGame(String userId, final String game, final Listener listener) {

        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                System.out.println("userAddGame - onSuccess game name = " + game);

              /*  UserData user = documentSnapshot.toObject(UserData.class);

                List<String> games = user.getGames();
                games.add(game);*/

                userRef.update("games", FieldValue.arrayUnion("games", game))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listener.onSuccess();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());
            }
        });

    }

    static public void getUserGames(String userId, final List<GameData> games, final RecyclerView.Adapter adapter) {

        Log.d("DatabaseManager", "getUserGames called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserGames OnSuccess called");

                UserData user = documentSnapshot.toObject(UserData.class);
                List<String> gameKeys = user.getGames();

                for (String gameKey : gameKeys) {
                    getGameFromDatabase(gameKey, games, adapter);
                }

                // adapter.notifyDataSetChanged();
            }
        });
    }

    static public void getUserGames(String userId, final List<GameData> games, final RecyclerView.Adapter adapter, final Listener listener) {

        Log.d("DatabaseManager", "getUserGames called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserGames OnSuccess called");

                UserData user = documentSnapshot.toObject(UserData.class);
                List<String> gameKeys = user.getGames();

                games.clear();
                adapter.notifyDataSetChanged();
                for (String gameKey : gameKeys) {
                    getGameFromDatabase(gameKey, games, adapter);
                }
                listener.onSuccess();

                // adapter.notifyDataSetChanged();
            }
        });
    }

    static public void getUserGames(String userId, final DataListener<List<GameData>> listener) {

        Log.d("DatabaseManager", "getUserGames called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserGames OnSuccess called");

                UserData user = documentSnapshot.toObject(UserData.class);
                List<String> gameKeys = user.getGames();

                final List<GameData> games = new ArrayList<>();
                for (String gameKey : gameKeys) {
                    getGameFromDatabase(gameKey, new DataListener<GameData>() {
                        @Override
                        public void onSuccess(GameData gameData) {
                            games.add(gameData);
                            listener.onSuccess(games);
                        }
                    });

                    // adapter.notifyDataSetChanged();
                }
            }
        });

    }

    static public void getUserFriends(final String userId, final List<UserData> users, final Listener listener) {
        Log.d("DatabaseManager", "getUserFriends called");

        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "getUserFriends OnSuccess called");

                UserData user = documentSnapshot.toObject(UserData.class);
                List<String> freinds = user.getFriends();

                users.clear();
                for (String friend : freinds) {
                    getUserFromDatabase(friend, users, listener);
                }

                // adapter.notifyDataSetChanged();
            }
        });
    }

    static public void getUsersFromList(List<Chatlist> chats, final List<UserData> users, final Listener listener) {
        Log.d("DatabaseManager", "getUsersFromList called");

        for (int i = 0; i < chats.size(); i++) {
            FirebaseFirestore.getInstance().collection("users").whereEqualTo("key", chats.get(i).getId())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Log.d("DatabaseManager", "getUsersFromList onSuccess called");

                    for (QueryDocumentSnapshot userDocument : queryDocumentSnapshots) {
                        UserData user = userDocument.toObject(UserData.class);

                        Log.d("DatabaseManager", "name  = " + user.getName());


                        users.add(user);
                    }

                    listener.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("DatabaseManager", e.getMessage());

                }
            });
        }

    }

    static public void getTopicsByGame(String guid, final List<ParentData> topics, final Listener listener) {
        Log.d("DatabaseManager", "getTopicsByGame called");

        CollectionReference topicsReff = FirebaseFirestore.getInstance().collection("games").document(guid).collection("topics");

        topicsReff.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("DatabaseManager", "getTopicsByGame onSuccess called");

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ParentData topic = document.toObject(ParentData.class);
                    topic.setId(document.getId());

                    Log.d("DatabaseManager", "topic name = " + topic.getTitle());

                    topics.add(topic);
                }

                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());
            }
        });

    }

    static public void getTopicsByGame(String guid, final DataListener<List<ParentData>> listener) {
        Log.d("DatabaseManager", "getTopicsByGame called");

        CollectionReference topicsReff = FirebaseFirestore.getInstance().collection("games").document(guid).collection("topics");

        topicsReff.orderBy("timestamp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("DatabaseManager", "getTopicsByGame onSuccess called");

                List<ParentData> topics = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ParentData topic = document.toObject(ParentData.class);
                    topic.setId(document.getId());

                    Log.d("DatabaseManager", "topic name = " + topic.getTitle());

                    topics.add(topic);
                }

                listener.onSuccess(topics);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());
            }
        });

    }

    static public void addTopicToDatabase(String guid, ParentData topic) {
        Log.d("DatabaseManager", "addTopicToDatabase called");

        CollectionReference colReff = FirebaseFirestore.getInstance().collection("games")
                .document(guid).collection("topics");

        String topicId = colReff.document().getId();
        topic.setId(topicId);
        colReff.document(topicId).set(topic);
   /*             .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        documentReference.update("id",documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });*/


    }

    static public void updateTopic(String guid, String topicID, final List<ChildData> comments) {
        Log.d("DatabaseManager", "UpdateTopic called");

        final DocumentReference topicReff = FirebaseFirestore.getInstance().collection("games")
                .document(guid).collection("topics").document(topicID);

        topicReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "UpdateTopic onSuccess called");

                if (documentSnapshot.exists()) {
                    topicReff.update("items", comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("DatabaseManager", "updateTopic - onSuccess - onSuccess");

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("DatabaseManager", e.getMessage());

                                }
                            });
                } else {
                    Log.d("DatabaseManager", documentSnapshot.getId() + " not exists!");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());

            }
        });
    }

    static public void updateTopic(String guid, String topicID, final List<ChildData> comments, final Listener listener) {
        Log.d("DatabaseManager", "UpdateTopic called");

        final DocumentReference topicReff = FirebaseFirestore.getInstance().collection("games")
                .document(guid).collection("topics").document(topicID);

        topicReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DatabaseManager", "UpdateTopic onSuccess called");

                if (documentSnapshot.exists()) {
                    topicReff.update("items", comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.onSuccess();
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DatabaseManager", e.getMessage());

            }
        });


    }

    static private void addGroupToDatabase(String userId, GroupData group, final Listener listener){

        CollectionReference groupsReff = FirebaseFirestore.getInstance().collection("groups");
        String groupdId = groupsReff.document().getId();
        group.setKey(groupdId);
        groupsReff.document(groupdId).set(group).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        });
    }

    static public void getGroupFromFS(String groupId, final DataListener<GroupData> listener){

        FirebaseFirestore.getInstance().collection("groups").document(groupId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        GroupData group = documentSnapshot.toObject(GroupData.class);

                        listener.onSuccess(group);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    static public void getUserGroupsFromFS(List<String> groupIds, final DataListener<List<GroupData>> listener){
        FirebaseFirestore.getInstance().collection("groups").whereArrayContains("groups", groupIds)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                List<GroupData> groups = new ArrayList<>();
                for(QueryDocumentSnapshot groupDoc : queryDocumentSnapshots){
                    GroupData group = groupDoc.toObject(GroupData.class);
                    groups.add(group);
                }

                listener.onSuccess(groups);

            }
        });
    }

    static public void updateProfileImage(String userId, final String imageUrl) {

        final DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);
        userReff.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            userReff.update("imageUrl", imageUrl);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    static public void updateUserData(String userId, String aboutMe, Map<String, Boolean> language, String birthday, String gender,double mLatitude, double mLongitude) {
        final String TAG = "updateUserData";
        DocumentReference userReff = FirebaseFirestore.getInstance().collection("users").document(userId);

        if (aboutMe != null) {
            userReff.update("aboutMe", aboutMe);
            Log.d(TAG,"aboutMe = " + aboutMe);
        }

        if (language != null) {
            userReff.update("language", language);
            Log.d(TAG,"language = " + language);

        }

        if (birthday != null) {
            userReff.update("birthday_timestamp", birthday);
            Log.d(TAG,"birthday_timestamp = " + birthday);

        }

        if (gender != null) {
            userReff.update("gender", gender);
            Log.d(TAG,"gender = " + gender);

        }

        if(mLatitude != 0){
            userReff.update("mLatitude",mLatitude);
            Log.d(TAG,"mLatitude = " + mLatitude);

        }

        if(mLongitude != 0){
            userReff.update("mLongitude",mLongitude);
            Log.d(TAG,"mLongitude = " + mLongitude);

        }

    }

    private static List<UserData> filerPlayerList(UserData mUser, List<UserData> players, String maxBirthday, float maxDistance) {

        List<UserData> tempPlayers = new ArrayList<>();

        Location mLocation = new Location("");
        mLocation.setLatitude(mUser.getMyLatitude());
        mLocation.setLongitude(mUser.getMyLongitude());

        for (UserData player : players) {

            Location pLocation = new Location("");
            pLocation.setLatitude(player.getMyLatitude());
            pLocation.setLongitude(player.getMyLongitude());

            Log.d("DatabaseManager", "filerPlayerList");
            Log.d("DatabaseManager", "player.getBirthday_timestamp() = " + player.getBirthday_timestamp());
            Log.d("DatabaseManager", "maxBirthday = " + maxBirthday);
            Log.d("DatabaseManager", "player.getBirthday_timestamp().compareTo(maxBirthday) = " + player.getBirthday_timestamp().compareTo(maxBirthday));

            Log.d("DatabaseManager", "maxDistance = " + maxDistance);
            Log.d("DatabaseManager", "mLocation.distanceTo(pLocation) = " + mLocation.distanceTo(pLocation));


            if (player.getBirthday_timestamp().compareTo(maxBirthday) >= 0 && maxDistance <= mLocation.distanceTo(pLocation)) {
                tempPlayers.add(player);
            }
        }

        return tempPlayers;
    }


}
