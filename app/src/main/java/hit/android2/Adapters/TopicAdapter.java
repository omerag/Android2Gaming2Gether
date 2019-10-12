package hit.android2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Database.ChildData;
import hit.android2.Database.DatabaseManager;
import hit.android2.Database.ParentData;
import hit.android2.R;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private Context context;
    private List<ParentData> topics;

    private int counter = 0;

    public TopicAdapter(Context context,List<ParentData> topics) {
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parent,parent,false);
        TopicViewHolder viewHolder = new TopicViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {


        holder.title.setText(topics.get(position).getTitle());
        DatabaseManager.loadGameIntoViews(topics.get(position).getGame_key(),holder.gameTextView,holder.gameImage,context);
      //  holder.gameTextView.setText(topics.get(position).);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }



    class TopicViewHolder extends RecyclerView.ViewHolder {

        int pos = counter;

        TextView title;
        LinearLayout commentLayout;
        EditText commentEditText;
        TextView gameTextView;
        ImageView gameImage;

        RecyclerView recyclerView;
        CommentAdapter commentAdapter;
        List<ChildData> comments;
        ImageButton sendBtn;


        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_parent_item_topic_name);
            commentEditText = itemView.findViewById(R.id.parent_item_comment_edit_text);
            recyclerView = itemView.findViewById(R.id.item_parent_recycler);
            gameTextView = itemView.findViewById(R.id.tv_parent_item_game_name);
            gameImage = itemView.findViewById(R.id.tv_parent_item_game_image);


            commentLayout = itemView.findViewById(R.id.parent_item_comment_layout);
            sendBtn = itemView.findViewById(R.id.home_fragment_comment_Button);
            comments = topics.get(pos).getItems();
            counter++;
            commentAdapter = new CommentAdapter(context,comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(commentAdapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, title.getText().toString() + " clicked", Toast.LENGTH_SHORT).show();

                    if(commentAdapter.isOpen()){
                        commentLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        commentAdapter.setOpen(false);
                    }
                    else {
                        commentLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);

                        commentAdapter.setOpen(true);
                    }
                    commentAdapter.notifyDataSetChanged();

                }
            });


            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    comments.add(new ChildData(FirebaseAuth.getInstance().getCurrentUser().getUid(),commentEditText.getText().toString()));
                    //DatabaseManager.updateTopic("",topics.get(pos).getId(),comments);
                }
            });
        }


    }
}
