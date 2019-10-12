package hit.android2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hit.android2.Database.ChildData;
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


        holder.massage.setText(topics.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }



    class TopicViewHolder extends RecyclerView.ViewHolder {

        TextView massage;
        LinearLayout commentLayout;

        RecyclerView recyclerView;
        CommentAdapter commentAdapter;
        List<ChildData> comments;


        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            massage = itemView.findViewById(R.id.tv_parent_item_topic_name);
            recyclerView = itemView.findViewById(R.id.item_parent_recycler);
            commentLayout = itemView.findViewById(R.id.parent_item_comment_layout);
            comments = topics.get(counter).getItems();
            counter++;
            commentAdapter = new CommentAdapter(context,comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(commentAdapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, massage.getText().toString() + " clicked", Toast.LENGTH_SHORT).show();

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
        }


    }
}
