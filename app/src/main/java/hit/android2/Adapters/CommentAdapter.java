package hit.android2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hit.android2.Database.Model.ChildData;
import hit.android2.Database.DatabaseManager;
import hit.android2.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<ChildData> comments;
    private boolean isOpen = false;


    public CommentAdapter(Context context, List<ChildData> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_child,parent,false);
        CommentAdapter.CommentViewHolder viewHolder = new CommentAdapter.CommentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if(holder.massage == null){
            System.out.println("holder.massage == null");
        }

        holder.massage.setText(comments.get(position).getMassage());
        DatabaseManager.getUserFromDatabase(comments.get(position).getUser_key(),null,holder.userName,holder.userImage,context);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView massage;
        TextView userName;
        ImageView userImage;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tv_child_item_user_name);
            massage = itemView.findViewById(R.id.tv_child_item_massage);
            userImage = itemView.findViewById(R.id.tv_child_item_user_image);

        }
    }

}
