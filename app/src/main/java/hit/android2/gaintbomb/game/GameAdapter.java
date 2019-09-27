package hit.android2.gaintbomb.game;

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

import hit.android2.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameItemViewHolder>{

    private Context context;
    private List<GameItem> gameItemList;

    public GameAdapter(Context context, List<GameItem> gameItemList) {
        this.context = context;
        this.gameItemList = gameItemList;

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

        Glide.with(context).load(gameItemList.get(position).getImageUrl()).into(holder.imageViewGame);

        holder.textViewName.setText(gameItemList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        if(gameItemList != null){
            return gameItemList.size();
        }
        return 0;
    }

    public class GameItemViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        ImageView imageViewGame;

        public GameItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.game_name);
            imageViewGame = itemView.findViewById(R.id.game_image);
        }
    }

}
