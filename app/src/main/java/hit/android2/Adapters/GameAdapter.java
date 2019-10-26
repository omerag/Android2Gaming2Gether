package hit.android2.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hit.android2.Database.Model.GameData;
import hit.android2.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameItemViewHolder> implements View.OnClickListener {

    private Context context;
    private List<GameData> gameDataList;
    private GameData game = null;
    private AdapterListener listener;

    public GameAdapter(Context context, List<GameData> gameDataList) {
        this.context = context;
        this.gameDataList = gameDataList;

        System.out.println("GameAdapter created");
    }

    public interface AdapterListener{
        void onClick(View view, int position);

    }

    public void setListener(AdapterListener listener){
        this.listener = listener;

    }

    public GameAdapter(Context context, List<GameData> gameDataList,GameData game) {
        this.context = context;
        this.gameDataList = gameDataList;
        this.game = game;

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
        holder.positionTextView.setText(String.valueOf(position));
        holder.textViewName.setText(gameDataList.get(position).getName());

        Log.d("GameAdapter","onBindViewHolder - gameDataList.get(position).getImageUrl() = " + gameDataList.get(position).getImageUrl());

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
        TextView positionTextView;

        public GameItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.game_name);
            imageViewGame = itemView.findViewById(R.id.game_image);
            positionTextView = itemView.findViewById(R.id.game_position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener != null){
                        listener.onClick(view,getAdapterPosition());
                    }

                }
            });
        }
    }

    public List<GameData> getGameDataList() {
        return gameDataList;
    }

    public void setGameDataList(List<GameData> gameDataList) {
        this.gameDataList = gameDataList;
    }
}
