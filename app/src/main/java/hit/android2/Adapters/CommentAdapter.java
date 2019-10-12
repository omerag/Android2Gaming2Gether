package hit.android2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hit.android2.Database.ChildData;
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


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            massage = itemView.findViewById(R.id.tv_child_item_massage);
        }
    }

}
