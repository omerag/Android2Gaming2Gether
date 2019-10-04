package hit.android2.gaintbomb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import hit.android2.Database.DatabaseManager;
import hit.android2.Database.GameData;
import hit.android2.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameItemViewHolder> implements View.OnClickListener {

    private Context context;
    private List<GameData> gameDataList;
    private String state;

    public GameAdapter(Context context, List<GameData> gameDataList, String state) {
        this.context = context;
        this.gameDataList = gameDataList;
        this.state = state;

        System.out.println("GameAdapter created");

    }

    @NonNull
    @Override
    public GameItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_card_layout,parent,false);
        GameItemViewHolder viewHolder = new GameItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameItemViewHolder holder, int position) {

        Glide.with(context).load(gameDataList.get(position).getImageUrl()).into(holder.imageViewGame);
        holder.guidTextView.setText(gameDataList.get(position).getGuid());
        holder.textViewName.setText(gameDataList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        if(gameDataList != null){
            return gameDataList.size();
        }
        return 0;
    }

    @Override
    public void onClick(View view) {

    }

    public class GameItemViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        ImageView imageViewGame;
        TextView guidTextView;

        public GameItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.game_name);
            imageViewGame = itemView.findViewById(R.id.game_image);
            guidTextView = itemView.findViewById(R.id.game_guid);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(state.equals("profile")){
                        Toast.makeText(context, "profile game", Toast.LENGTH_SHORT).show();


                    }
                    else if(state.equals("search game")){

                        DatabaseManager.userAddGame(FirebaseAuth.getInstance().getCurrentUser().getUid(),guidTextView.getText().toString());

                        Toast.makeText(context, "search game", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}
