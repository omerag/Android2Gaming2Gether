package hit.android2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import hit.android2.Database.Model.ChildData;
import hit.android2.R;

public class FSCommentsAdapter extends FirestoreRecyclerAdapter<ChildData,FSCommentsAdapter.CommentViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FSCommentsAdapter(@NonNull FirestoreRecyclerOptions<ChildData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull ChildData model) {
      //  holder.userName.setText(model.get);
        //holder.messege;
        //holder.userImage;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child,
                parent, false);
        return new CommentViewHolder(v);
    }



    class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        TextView messege;
        ImageView userImage;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tv_child_item_user_name);
            messege = itemView.findViewById(R.id.tv_child_item_massage);
            userImage = itemView.findViewById(R.id.tv_child_item_user_image);
        }
    }
}
