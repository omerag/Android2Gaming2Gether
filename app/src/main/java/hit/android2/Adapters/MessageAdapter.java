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
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import hit.android2.Database.FirebaseManager;
import hit.android2.Model.Chat;
import hit.android2.R;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<Chat> mChat;
    FirebaseManager manager = new FirebaseManager();
    FirebaseUser fuser;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context mContext, List<Chat> mChat){
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == MSG_TYPE_RIGHT) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
                return new MessageAdapter.ViewHolder(view);
            }
            else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
                return new MessageAdapter.ViewHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = manager.getFireBaseAuth().getCurrentUser();

        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
