package hit.android2.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import hit.android2.Database.CommentDataHolder;
import hit.android2.Database.Managers.FirebaseManager;
import hit.android2.Database.Managers.MessegingManager;
import hit.android2.Database.Model.ChildData;
import hit.android2.Database.Managers.DatabaseManager;
import hit.android2.Database.Model.ParentData;
import hit.android2.Database.Model.UserData;
import hit.android2.Database.TopicDataHolder;
import hit.android2.HomeFragmentLiveData;
import hit.android2.R;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private Context context;
    private List<ParentData> databaseTopics;
    private List<TopicDataHolder> topics;
    private AdapterListener listener;
    private HomeFragmentLiveData liveData;

    private int counter = 0;

    public TopicAdapter(Context context, /*List<ParentData> topics*/ List<TopicDataHolder> topics, List<ParentData> databaseTopics, HomeFragmentLiveData liveData) {
        this.context = context;
        this.topics = topics;
        this.liveData = liveData;
        this.databaseTopics = databaseTopics;
    }

    public interface AdapterListener{
        void onClick(View view, int position);

    }

    public void setListener(AdapterListener listener){
        this.listener = listener;

    }

/*    public List<ParentData> getTopics() {
        return topics;
    }*/

    public List<TopicDataHolder> getTopics() {
        return topics;
    }



/*    public void setTopics(List<ParentData> topics) {
        this.topics = topics;
    }*/

    public void setTopics(List<TopicDataHolder> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parent,parent,false);
        TopicViewHolder viewHolder = new TopicViewHolder(view);
        counter++;


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {


        holder.initRecycleview(position);
        holder.title.setText(topics.get(position).getTitle());
        holder.gameTextView.setText(topics.get(position).getGameName());
        holder.userTextView.setText(topics.get(position).getTopicsOwner());
        holder.dateTextView.setText(topics.get(position).getDate());


        //DatabaseManager.loadGameIntoViews(topics.get(position).getGame_key(),holder.gameTextView,holder.gameImage,context);
        Glide.with(context).load(topics.get(position).getImageUrl()).into(holder.gameImage);
      //  holder.gameTextView.setText(topics.get(position).);
    }

    @Override
    public int getItemCount() {
        if(topics != null){
            return topics.size();
        }
        return 0;
    }



    class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout commentLayout;
        EditText commentEditText;
        TextView gameTextView;
        TextView userTextView;
        ImageView gameImage;
        ImageView arrowIv;
        TextView dateTextView;

        RecyclerView recyclerView;
        CommentAdapter commentAdapter;
        //List<ChildData> comments;
        List<CommentDataHolder> comments;
        ImageButton sendBtn;


        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            int pos = counter;
            Log.d("TopicAdapter","pos = " + pos + ",counter = " + counter);

            title = itemView.findViewById(R.id.tv_parent_item_topic_name);
            commentEditText = itemView.findViewById(R.id.parent_item_comment_edit_text);
            recyclerView = itemView.findViewById(R.id.item_parent_recycler);
            userTextView = itemView.findViewById(R.id.tv_parent_item_topic_owner);
            gameTextView = itemView.findViewById(R.id.tv_parent_item_game_name);
            gameImage = itemView.findViewById(R.id.tv_parent_item_game_image);
            dateTextView = itemView.findViewById(R.id.tv_parent_item_time);
            arrowIv = itemView.findViewById(R.id.topic_arrow);


            commentLayout = itemView.findViewById(R.id.parent_item_comment_layout);
            sendBtn = itemView.findViewById(R.id.home_fragment_comment_Button);
            //comments = topics.get(pos).getItems();
          /*  comments = topics.get(pos).getComments();
            commentAdapter = new CommentAdapter(context,comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(commentAdapter);*/

            //DatabaseManager.loadGameIntoViews(topics.get(getAdapterPosition()).getGame_key(),gameTextView,gameImage,context);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(commentAdapter.isOpen()){

                        recyclerView.animate()
                                .alpha(0.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                });

                        commentLayout.animate()
                                .alpha(0.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        commentLayout.setVisibility(View.GONE);
                                    }
                                });


                        // commentLayout.setVisibility(View.GONE);
                       // recyclerView.setVisibility(View.GONE);
                        commentAdapter.setOpen(false);
                        arrowIv.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_down));
                    }
                    else {
                        recyclerView.animate()
                                .alpha(1.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                });

                        commentLayout.animate()
                                .alpha(1.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        commentLayout.setVisibility(View.VISIBLE);
                                    }
                                });


                        //commentLayout.setVisibility(View.VISIBLE);
                        //recyclerView.setVisibility(View.VISIBLE);

                        commentAdapter.setOpen(true);
                        arrowIv.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_up));

                    }
                    commentAdapter.notifyDataSetChanged();

                }
            });


            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseTopics.get(getAdapterPosition()).getItems().add(new ChildData(commentEditText.getText().toString(),System.currentTimeMillis(),FirebaseAuth.getInstance().getCurrentUser().getUid()));

                    DatabaseManager.getUserFromDatabase(FirebaseManager.getCurrentUserId(), new DatabaseManager.DataListener<UserData>() {
                        @Override
                        public void onSuccess(UserData userData) {
                            CommentDataHolder comment = new CommentDataHolder(userData.getName(),commentEditText.getText().toString(),userData.getImageUrl());
                            comments.add(comment);
                            //notifyDataSetChanged();
                            commentAdapter.notifyDataSetChanged();

                            MessegingManager.notifyNewCommentOnTopic(context,topics.get(getAdapterPosition()),comment);
                            MessegingManager.subscribeToTopic(topics.get(getAdapterPosition()).getTopicId());
                            DatabaseManager.updateTopic(topics.get(getAdapterPosition()).getGameId(), topics.get(getAdapterPosition()).getTopicId(), databaseTopics.get(getAdapterPosition()).getItems());

                        }
                    });

                }
            });
        }

        public void initRecycleview(int position){
            comments = topics.get(position).getComments();
            commentAdapter = new CommentAdapter(context,comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(commentAdapter);
        }






    }


}
