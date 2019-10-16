package hit.android2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hit.android2.R;

public class CharacterSelectAdapter extends RecyclerView.Adapter<CharacterSelectAdapter.CharacterSelectViewHolder> {

    private Context context;
    private List<String> imageUrlList;
    private Listener listener;

    public CharacterSelectAdapter(Context context, List<String> imageUrl) {
        this.context = context;
        this.imageUrlList = imageUrl;
    }

    public interface Listener{

        void onClick(String s);
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CharacterSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_character_layout,parent,false);
        CharacterSelectViewHolder viewHolder = new CharacterSelectViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterSelectViewHolder holder, int position) {

        Glide.with(context).load(imageUrlList.get(position)).into(holder.characterImage);

    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public class CharacterSelectViewHolder extends RecyclerView.ViewHolder{

        ImageView characterImage;

        public CharacterSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            characterImage = itemView.findViewById(R.id.card_character_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onClick(imageUrlList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
