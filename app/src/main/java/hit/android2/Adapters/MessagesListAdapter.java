package hit.android2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Model.UserData;
import hit.android2.R;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.UserDataViewHolder> implements View.OnClickListener{

    private Context context;
    private List<UserData> userDataList;
    private MessagesListAdapter.AdapterListener listener;
    private FirebaseManager manager = new FirebaseManager();

    public MessagesListAdapter(Context context, List<UserData> userDataList) {
        this.context = context;
        this.userDataList = userDataList;

        System.out.println("UserAdapter created");

    }

    public interface AdapterListener{
        void onClick(View view, int position);

    }

    public void setListener(MessagesListAdapter.AdapterListener listener){
        this.listener = listener;

    }

    public List<UserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    @NonNull
    @Override
    public MessagesListAdapter.UserDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messages_card_layout,parent,false);
        MessagesListAdapter.UserDataViewHolder viewHolder = new MessagesListAdapter.UserDataViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesListAdapter.UserDataViewHolder holder, final int position) {

        Glide.with(context).load(userDataList.get(position).getImageUrl()).into(holder.imageViewUserProfile);

        holder.textViewName.setText(userDataList.get(position).getName());
        manager.GetLastMessage(userDataList.get(position).getKey(),holder.textViewLastMessage);

    }

    @Override
    public int getItemCount() {
        if(userDataList != null){
            return userDataList.size();
        }
        return 0;
    }

    @Override
    public void onClick(View view) {

    }

    public class UserDataViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        TextView textViewLastMessage;
        ImageView imageViewUserProfile;

        public UserDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.user_name_tv);
            textViewLastMessage = itemView.findViewById(R.id.last_message_tv);
            imageViewUserProfile = itemView.findViewById(R.id.user_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onClick(view,getAdapterPosition());
                    }

                }
            });
        }
    }
}
