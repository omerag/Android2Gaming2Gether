package hit.android2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hit.android2.Adapters.GroupAdapter;
import hit.android2.Adapters.UserAdapter;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Managers.MessegingManager;
import hit.android2.Database.Managers.StorageManager;
import hit.android2.Database.Model.GroupData;
import hit.android2.Database.Model.UserData;

public class GroupMessagesFragment extends Fragment {

    private FloatingActionButton addNewGroupBtn;
    private RecyclerView groupsRecyclerview;
    private CircleImageView groupPicView;

    FirebaseManager manager = new FirebaseManager();
    private GroupAdapter groupAdapter;

    private List<String> group_users_id;
    private List<String> mUserGroups;
    private List<GroupData> mGroups;
    private List<Boolean> isSelected;
    private int finalI;

    private String group_name;
    private String group_image_URL;
    private int CAMERA_CODE = 0;
    private int GALLERY_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_chat_fragment, container, false);

        addNewGroupBtn = view.findViewById(R.id.floating_action_btn);
        groupsRecyclerview = view.findViewById(R.id.group_messages_fragment_recycler);
        groupsRecyclerview.setHasFixedSize(true);
        groupsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserGroups = new ArrayList<>();
        mGroups = new ArrayList<>();
        groupAdapter = new GroupAdapter(getContext(), mGroups);
        groupsRecyclerview.setAdapter(groupAdapter);

        AddNewGroupBtnListener newGroupBtnListener = new AddNewGroupBtnListener();
        addNewGroupBtn.setOnClickListener(newGroupBtnListener);

        getUserGroups();

        groupAdapter.setListener(new GroupAdapter.GroupAdapterListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), GroupMessagingActivity.class);
                intent.putExtra("group_name",mGroups.get(position).getGroup_name());
                intent.putExtra("group_image",mGroups.get(position).getImage_URL());
                intent.putExtra("groupId", mGroups.get(position).getKey());
                getActivity().startActivity(intent);
            }
        });



        return view;
    }


    public class AddNewGroupBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            final Dialog dialog = new Dialog(getActivity());

            dialog.setContentView(R.layout.new_group_dialog);
            dialog.setTitle("Create Group");

            final List<UserData> friends = new ArrayList<>();


            final TextInputEditText groupNameET = dialog.findViewById(R.id.group_name_ET);
            final RecyclerView friendsRecycler = dialog.findViewById(R.id.friends_recycler_view);
            friendsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            final UserAdapter userAdapter = new UserAdapter(getContext(),friends);
            final Button createGroupBtn = dialog.findViewById(R.id.create_group_btn);
            final Button addGroupPicBtn = dialog.findViewById(R.id.add_group_pic_btn);
            groupPicView = dialog.findViewById(R.id.group_pic_view);
            group_users_id = new ArrayList<>();

            DatabaseManager.getUserFriends(manager.getFireBaseAuth().getCurrentUser().getUid(), friends, new DatabaseManager.Listener() {
                @Override
                public void onSuccess() {

                    friendsRecycler.setAdapter(userAdapter);
                    friendsRecycler.setHasFixedSize(true);
                    userAdapter.notifyDataSetChanged();
                }
            });

            isSelected = new ArrayList<>();
            for (int i = 0; i < 50; i++)
            {
                isSelected.add(i,false);
            }

            userAdapter.setListener(new UserAdapter.AdapterListener() {
                @Override
                public void onClick(View view, int position) {

                    if (isSelected.get(position).equals(false))
                    {
                        view.setBackgroundColor(Color.rgb(149,149,149));
                        isSelected.set(position, true);
                        //group_users_id.add(friends.get(position).getKey());
                    }
                    else {
                        view.setBackgroundColor(Color.rgb(255,255,255));
                        isSelected.set(position, false);

                    }
                }
            });

            addGroupPicBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(getContext(), addGroupPicBtn);
                    popupMenu.getMenuInflater().inflate(R.menu.group_pic_pop_up_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case R.id.group_menu_item_camera:
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, CAMERA_CODE);
                                    break;
                                case R.id.group_menu_item_gallery:
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, GALLERY_CODE);
                                    break;
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                }
            });

            createGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     group_name = groupNameET.getText().toString();

                    if (TextUtils.isEmpty(group_name))
                    {
                        Toast.makeText(getContext(), "Enter group name...", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //group_image_URL = "https://cdn.images.express.co.uk/img/dynamic/galleries/517x/370884.jpg";

                        for (int i = 0; i < isSelected.size(); i++)
                        {
                            if (isSelected.get(i))
                            {
                                group_users_id.add(friends.get(i).getKey());
                            }
                        }
                        String myId = manager.getFireBaseAuth().getCurrentUser().getUid();
                        group_users_id.add(myId);
                        GroupData groupData = new GroupData(group_name,group_image_URL,group_users_id);
                        DatabaseManager.addGroupToDatabase(myId, groupData, new DatabaseManager.DataListener<String>() {
                            @Override
                            public void onSuccess(String s) {

                                Snackbar.make(getView(),"Group created", 3000).show();
                                addGroupToUsersChatList(s);
                                addGroupToGroupChats(s);
                            }
                        });
                        dialog.dismiss();
                    }
                }
            });

            dialog.setCanceledOnTouchOutside(false);

            dialog.show();
        }
    }


    private void addGroupToUsersChatList(final String group_id) {

        for (int i = 0; i < group_users_id.size(); i++){

            finalI = i;
            DatabaseManager.addGroupToUser(group_users_id.get(i), group_id, new DatabaseManager.Listener(){

                @Override
                public void onSuccess() {
                    DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
                        @Override
                        public void onSuccess(UserData userData) {

                            String messege = userData.getName() + "has invited you to the group " + group_name;
                            MessegingManager.notifyNewMessegInChat(getActivity(),userData.getName(),group_users_id.get(finalI),messege);

                        }
                    });

                }
            });

        }
    }

    private void loadGroups() {


       DatabaseManager.getUserGroupsFromFS(mUserGroups, new DatabaseManager.DataListener<List<GroupData>>() {
           @Override
           public void onSuccess(List<GroupData> groupData) {

                mGroups = groupData;
                groupAdapter.setGroupDataList(mGroups);
                groupAdapter.notifyDataSetChanged();

               Log.d("GroupsSize",groupData.size()+"");
           }
       });
    }

    private void getUserGroups()
    {
        String myId = manager.getFireBaseAuth().getCurrentUser().getUid();

        DatabaseManager.getUserFromDatabase(myId, new DatabaseManager.DataListener<UserData>() {
            @Override
            public void onSuccess(UserData userData) {

                mUserGroups = userData.getGroups();
                loadGroups();
            }
        });
    }

        private void addGroupToGroupChats(final String group_id)
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChats").child(group_id);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists())
                {
                    reference.setValue(group_id);
                }

                getUserGroups();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap image_bitmap = (Bitmap) extras.get("data");
                groupPicView.setImageBitmap(image_bitmap);
                StorageManager.uploadImageFromImageviewReturnUrl(groupPicView,group_name, new StorageManager.DataListener<String>() {
                    @Override
                    public void onSuccess(String url) {
                        group_image_URL = url;
                    }
                });
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
                            groupPicView.setImageBitmap(bitmap);
                            StorageManager.uploadImageFromImageviewReturnUrl(groupPicView,group_name, new StorageManager.DataListener<String>() {
                                @Override
                                public void onSuccess(String url) {
                                    group_image_URL = url;
                                }
                            });
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

}
