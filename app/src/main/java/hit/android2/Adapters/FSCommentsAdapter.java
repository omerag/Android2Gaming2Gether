package hit.android2.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import hit.android2.Database.Model.ChildData;

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

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }



    class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        TextView messege;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
