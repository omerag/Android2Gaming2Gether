package hit.android2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hit.android2.R;

public class FlagAdapter extends RecyclerView.Adapter<FlagAdapter.FlagViewHolder> {

    private List<Integer> flagsDrawable;
    private List<String> languages;

    private Listener listener;

    public FlagAdapter() {
        initList();
    }

    public interface Listener{
        void onClick(String language);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friends_dialog_flags_recycler,parent,false);
        FlagViewHolder viewHolder = new FlagViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlagViewHolder holder, int position) {
        holder.imageView.setImageResource(flagsDrawable.get(position));
    }



    @Override
    public int getItemCount() {
        return flagsDrawable.size();
    }

    public class FlagViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public FlagViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_friends_dialog_flag_iv);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onClick(languages.get(getAdapterPosition()));
                    }
                }
            });
        }

    }

    private void initList(){
        flagsDrawable = new ArrayList<>();
        languages = new ArrayList<>();
        flagsDrawable.add(R.drawable.ic_flag_israel);
        languages.add("language.Hebrew");
        flagsDrawable.add(R.drawable.ic_flag_usa);
        languages.add("language.English");
        flagsDrawable.add(R.drawable.ic_flag_france);
        languages.add("language.French");
        flagsDrawable.add(R.drawable.ic_flag_china);
        languages.add("language.Chinese");
        flagsDrawable.add(R.drawable.ic_flag_germany);
        languages.add("language.German");
        flagsDrawable.add(R.drawable.ic_flag_italy);
        languages.add("language.Italian");
        flagsDrawable.add(R.drawable.ic_flag_japan);
        languages.add("language.Japanese");
        flagsDrawable.add(R.drawable.ic_flag_russia);
        languages.add("language.Russian");
        flagsDrawable.add(R.drawable.ic_flag_south_korea);
        languages.add("language.Korean");
        flagsDrawable.add(R.drawable.ic_flag_turkey);
        languages.add("language.Arabic");

    }

}
