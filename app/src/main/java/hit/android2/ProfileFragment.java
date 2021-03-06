package hit.android2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hit.android2.Adapters.CharacterSelectAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.UserData;
import hit.android2.Database.Managers.StorageManager;
import hit.android2.gaintbomb.api.DataLoader;
import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.Model.GameData;

public class ProfileFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    private TextView usernameTv;
    private TextView aboutMeTv;
    private ImageView userIv;
    private TextView ageTv;
    //private TextView genderTv;
    private ImageView gender_img;


    private ImageButton pic_edit_btn;
    private ImageButton profile_edit_btn;
    private ImageButton editBtn;

    private List<GameData> gameDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;


    private ListenerRegistration listenerRegistration;
    private ProfileFragmentLiveData liveData;
    private DocumentReference userReff;

    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;

    private boolean isLogIn = false;
    private boolean isEdit = false;

    private boolean isGameRecycleOnScreen = true;

    private int CAMERA_CODE = 0;
    private int GALLERY_CODE = 1;

    private ArrayList<String> localUserGameList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("ProfileFragment", "onActivityCreated");

        liveData = ViewModelProviders.of(this).get(ProfileFragmentLiveData.class);

        View view = getActivity().findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView = (BottomNavigationView) view;

        View view1 = getActivity().findViewById(R.id.fragment_container);
        pager = (ViewPager) view1;

        floatingActionButton = getView().findViewById(R.id.floating_action_btn);
        usernameTv = getView().findViewById(R.id.profile_fragment_user_name);
        userIv = getView().findViewById(R.id.user_profile_img);
        aboutMeTv = getView().findViewById(R.id.about_me_tv);
        ageTv = getView().findViewById(R.id.age_tv);
        //genderTv = getView().findViewById(R.id.gender_tv);
        gender_img = getView().findViewById(R.id.gender_image);


        pic_edit_btn = getView().findViewById(R.id.image_edit_btn);
        profile_edit_btn = getView().findViewById(R.id.profile_edit_btn);
        editBtn = getView().findViewById(R.id.root_profile_edit_btn);

        FloatingBtnListener floatingBtnListener = new FloatingBtnListener();
        floatingActionButton.setOnClickListener(floatingBtnListener);

        ProfileImageEditListener profileImageEditListener = new ProfileImageEditListener();
        pic_edit_btn.setOnClickListener(profileImageEditListener);

        ProfileEditBtnListener profileEditBtnListener = new ProfileEditBtnListener();
        profile_edit_btn.setOnClickListener(profileEditBtnListener);

        EditBtnListener editBtnListener = new EditBtnListener();
        editBtn.setOnClickListener(editBtnListener);


        RecyclerView recyclerView = getView().findViewById(R.id.profile_fragment_recycler_games);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (liveData.getGameDataList() == null) {
            gameDataList = new ArrayList<>();
            liveData.setGameDataList(gameDataList);
        }
        gameAdapter = new GameAdapter(getActivity(), gameDataList); //gameDataList is empty, needs to be loaded from server
        recyclerView.setAdapter(gameAdapter);
        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dialog_fragments_container, new GameTopicsFragment(gameDataList.get(position).getGuid()))
                        .addToBackStack("gameTopicsFragment").commit();

                bottomNavigationView.setVisibility(View.INVISIBLE);
                pager.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onLongClick(View view, int position) {
                AlertDialog alertDialog= new MaterialAlertDialogBuilder(getContext())
                        .setTitle(getString(R.string.delete_game_title))
                        .setMessage(getString(R.string.delete_game_msg_1) + gameDataList.get(position).getName() + getString(R.string.delete_game_msg_2))
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //delete game
                                if(FirebaseManager.isLoged()) {
                                    DatabaseManager.userRemoveGame(FirebaseManager.getCurrentUserId(), gameDataList.get(position).getGuid(), new DatabaseManager.Listener() {
                                        @Override
                                        public void onSuccess() {


                                            if (position < gameDataList.size()) {
                                                gameDataList.remove(position);

                                            }
                                            gameAdapter.notifyDataSetChanged();

                                        }


                                    });
                                }
                                else {
                                    if (position < gameDataList.size()) {
                                        gameDataList.remove(position);
                                        localUserGameList.remove(position);
                                        saveGameListToSharedPrefernce();

                                    }
                                    gameAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        gameAdapter.notifyDataSetChanged();
        //user logged in


    }



    @Override
    public void onStart() {
        super.onStart();

    /*    if (gameAdapter != null) {
            gameAdapter.notifyDataSetChanged();
        }

        if (FirebaseManager.isLoged()) {
            loadUserGamesFromDB();
            loadUserProfileData();
        } else {
            loadGameListFromSharedPrefernce();
        }*/


        if (FirebaseManager.isLoged()) {
            userReff = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            listenerRegistration = userReff.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    Log.d("ProfileFragment", "onStart -listenerRegistration.onEvent - ");
                    Log.d("ProfileFragment", "userId =  " + documentSnapshot.getId());
                    if (e != null) {

                        Log.d("DatabaseManager", e.getMessage());
                        return;
                    }
                    System.out.println("documentSnapshot.id = " + documentSnapshot.getId());
                    UserData userData = documentSnapshot.toObject(UserData.class);

                    // listener.onSuccess(user);
                  /*  usernameTv.setText(user.getName());
                    liveData.setUsernameTv(user.getName());
                    aboutMeTv.setText(user.getAboutMe());
                    liveData.setAboutMeTv(user.getAboutMe());
                    // Glide.with(getActivity()).load(user.getImageUrl()).into(userIv);
                    liveData.setUserIv(user.getImageUrl());
                    Glide.with(getActivity()).load(liveData.getUserIv()).into(userIv);*/
                    if(userData == null){
                        return;
                    }
                    liveData.setUsernameTv(userData.getName());
                    liveData.setAboutMeTv(userData.getAboutMe());
                    liveData.setUserIv(userData.getImageUrl());
                    liveData.setBirthday(userData.getBirthday_timestamp());
                    liveData.setLevel(String.valueOf(userData.getTotalRank()));
                    liveData.setGender(userData.getGender());


                    usernameTv.setText(liveData.getUsernameTv());
                    aboutMeTv.setText(liveData.getAboutMeTv());
                    if(getActivity() != null) Glide.with(getActivity()).load(liveData.getUserIv()).into(userIv);
                    ageTv.setText(String.valueOf(liveData.getAge()));
                    String gender;
                    if(liveData.getGender() != null){
                        gender = liveData.getGender().equals("all") ? "--" : liveData.getGender();
                        if (gender.equals(""))
                        {
                            gender_img.setVisibility(View.INVISIBLE);
                        }
                        else if (gender.equals("male")){
                            gender_img.setImageResource(R.drawable.ic_male);
                        }
                        else {
                            gender_img.setImageResource(R.drawable.ic_female);
                    }


                    }
                    //genderTv.setText(liveData.getGender().equals("all") ? "--" : liveData.getGender());

                    gameDataList.clear();
                    gameAdapter.notifyDataSetChanged();
                    loadUserGamesFromDB();


                    DatabaseManager.getAllGamesFromByListFromDatabase(userData.getGames(),null);

                }//
            });
        }
        else {
            loadGameListFromSharedPrefernce();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (FirebaseManager.isLoged()) {
            if(listenerRegistration != null){
                listenerRegistration.remove();
            }
        }
    }

    private void loadUserProfileData() {

        DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
            @Override
            public void onSuccess(UserData userData) {

                liveData.setUsernameTv(userData.getName());
                liveData.setAboutMeTv(userData.getAboutMe());
                liveData.setUserIv(userData.getImageUrl());
                liveData.setBirthday(userData.getBirthday_timestamp());
                liveData.setLevel(String.valueOf(userData.getTotalRank()));
                liveData.setGender(userData.getGender());


                usernameTv.setText(liveData.getUsernameTv());
                aboutMeTv.setText(liveData.getAboutMeTv());
                if(getActivity() != null) Glide.with(getActivity()).load(liveData.getUserIv()).into(userIv);
                ageTv.setText(String.valueOf(liveData.getAge()));
                String gender = liveData.getGender().equals("all") ? "--" : liveData.getGender();
                if (gender.equals(""))
                {
                    gender_img.setVisibility(View.INVISIBLE);
                }
                else if (gender.equals("male")){
                    gender_img.setImageResource(R.drawable.ic_male);
                }
                else {
                    gender_img.setImageResource(R.drawable.ic_female);
                }
                //genderTv.setText(liveData.getGender().equals("all") ? "--" : liveData.getGender());
            }
        });
    }

    private void loadUserGamesFromDB() {

        DatabaseManager.getUserGames(FirebaseManager.getCurrentUserId(), gameDataList, gameAdapter, new DatabaseManager.Listener() {
            @Override
            public void onSuccess() {
                gameAdapter.notifyDataSetChanged();
            }
        });

    }

    class FloatingBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //showSearchDialog();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dialog_fragments_container, new SearchGameFragment(bottomNavigationView, pager, gameDataList, gameAdapter, liveData))
                    .addToBackStack("searchGame").commit();

            bottomNavigationView.setVisibility(View.INVISIBLE);
            pager.setVisibility(View.INVISIBLE);
        }
    }

    public class ProfileEditBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dialog_fragments_container, new ProfileEditDetailsFragment(bottomNavigationView, pager))
                    .addToBackStack("profileEditFragment").commit();

            bottomNavigationView.setVisibility(View.INVISIBLE);
            pager.setVisibility(View.INVISIBLE);
        }
    }


    private void loadUserGames() {

        if (liveData.getUserIv() == null || liveData.getUsernameTv() == null || liveData.getAboutMeTv() == null) {
            final UserData user = new UserData();
            DatabaseManager.getUserFromDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid(), user, usernameTv, userIv, getActivity(), new DatabaseManager.UserDataListener() {
                @Override
                public void onSuccess(UserData userData) {

                    aboutMeTv.setText(userData.getAboutMe());

                    liveData.setAboutMeTv(userData.getAboutMe());
                    liveData.setUsernameTv(userData.getName());
                    liveData.setUserIv(userData.getImageUrl());
                    Log.d("loadUserGame", "user profile url = " + liveData.getUserIv());
                    Glide.with(getActivity()).load(liveData.getUserIv()).into(userIv);


                }
            });
        } else {
            Log.d("loadUserGame", "user profile url = " + liveData.getUserIv());
            usernameTv.setText(liveData.getUsernameTv());
            Glide.with(getActivity()).load(liveData.getUserIv()).into(userIv);
            aboutMeTv.setText(liveData.getAboutMeTv());
        }

    }

    class ProfileImageEditListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            PopupMenu popupMenu = new PopupMenu(getContext(), pic_edit_btn);
            popupMenu.getMenuInflater().inflate(R.menu.poupup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.menu_item_camera:
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_CODE);
                            break;
                        case R.id.menu_item_gallery:
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, GALLERY_CODE);
                            break;
                        case R.id.menu_item_figures:
                            createCharacterImageSelectDialog();
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
    }

    void createCharacterImageSelectDialog() {


        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_select_character_image);

        dialog.setTitle("Select Character Dialog");
        final TextView dialog_title_tv = dialog.findViewById(R.id.dialog_title_tv);


        final RecyclerView recyclerView = dialog.findViewById(R.id.dialog_character_select_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext(),RecyclerView.HORIZONTAL,false));

        final List<String> characterUrlList = new ArrayList<>();
        final CharacterSelectAdapter characterSelectAdapter = new CharacterSelectAdapter(dialog.getContext(), characterUrlList);
        characterSelectAdapter.setListener(new CharacterSelectAdapter.Listener() {
            @Override
            public void onClick(String s) {
                DatabaseManager.updateProfileImage(FirebaseAuth.getInstance().getCurrentUser().getUid(), s);
                dialog.dismiss();
            }
        });


        GameAdapter gameAdapter = new GameAdapter(dialog.getContext(), gameDataList);
        gameAdapter.setListener(new GameAdapter.AdapterListener() {
            @Override
            public void onClick(View view, int position) {
                DataLoader loader = new DataLoader(BuildConfig.GiantBombApi, dialog.getContext());
                loader.getCharactersByGameRequest(gameDataList.get(position).getGuid(), new DataLoader.Listener() {
                    @Override
                    public void onSuccess(String string) {

                        if (isGameRecycleOnScreen) {
                            dialog_title_tv.setText(R.string.character_choose);
                            recyclerView.setAdapter(characterSelectAdapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(dialog.getContext(), 4));
                            isGameRecycleOnScreen = false;
                        }


                        characterUrlList.add(string);
                        characterSelectAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(gameAdapter);
        gameAdapter.notifyDataSetChanged();
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap image_bitmap = (Bitmap) extras.get("data");
                userIv.setImageBitmap(image_bitmap);
                StorageManager.uploadImageFromImageview(userIv);
            }
        }

        if (requestCode == GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                try {
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                    final int width = 256;//bitmap.getWidth() * 0.5;
                    final int height = 256;//bitmap.getHeight() * 0.5;

                    //loading resized bitmap into image view and uploading it to the server
                    new AsyncTask<String, Bitmap, Bitmap>() {
                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            userIv.setImageBitmap(bitmap);
                            StorageManager.uploadImageFromImageview(userIv);
                            Log.d("ProfileFragment", "bitmap updated");

                        }

                        @Override
                        protected Bitmap doInBackground(String... strings) {
                            Log.d("ProfileFragment", "Begin scaling bitmap");
                            Bitmap resize = resizeBitmap(bitmap, width, height);//Bitmap.createScaledBitmap(bitmap,width,height,true);
                            Log.d("ProfileFragment", "Ending scaling bitmap");

                            return resize;
                        }
                    }.execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private static Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    class EditBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (!isEdit) {
                pic_edit_btn.setVisibility(View.VISIBLE);
                profile_edit_btn.setVisibility(View.VISIBLE);
                isEdit = true;
            } else {
                pic_edit_btn.setVisibility(View.GONE);
                profile_edit_btn.setVisibility(View.GONE);
                isEdit = false;
            }
        }
    }

    private void saveGameListToSharedPrefernce() {
        SharedPreferences sp = getActivity().getSharedPreferences("game_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(localUserGameList);
        editor.putString("game_list", json);
        editor.apply();

    }

    private void loadGameListFromSharedPrefernce() {
        SharedPreferences sp = getActivity().getSharedPreferences("sp", 0);
        Gson gson = new Gson();
        String json = sp.getString("game_list", "");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        localUserGameList = gson.fromJson(json, type);

        if (localUserGameList != null) {
            for (int i = 0; i < localUserGameList.size(); i++) {
                gameDataList.clear();
                DatabaseManager.getGameFromDatabase(localUserGameList.get(i), new DatabaseManager.DataListener<GameData>() {
                    @Override
                    public void onSuccess(GameData gameData) {
                        gameDataList.add(gameData);
                        gameAdapter.setGameDataList(gameDataList);
                        gameAdapter.notifyDataSetChanged();
                    }
                });
                Log.d("ProfileFragment", "localGameList = " + localUserGameList.toString());

            }

        }
    }
}
