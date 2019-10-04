package hit.android2.gaintbomb.Adapters;

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

import hit.android2.Database.UserData;
import hit.android2.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserDataViewHolder> implements View.OnClickListener{

    private Context context;
    private List<UserData> userDataList;

    public UserAdapter(Context context, List<UserData> userDataList) {
        this.context = context;
        this.userDataList = userDataList;

        System.out.println("UserAdapter created");

    }

    @NonNull
    @Override
    public UserDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_card_layout,parent,false);
        UserDataViewHolder viewHolder = new UserDataViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataViewHolder holder, int position) {

        Glide.with(context).load(userDataList.get(position).getImageUrl()).into(holder.imageViewGame);

        holder.textViewName.setText(userDataList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        if(userDataList != null){
            return userDataList.size();
        }
        return 0;
    }

    @Override
    public void onClick(View view) {

    }

    public class UserDataViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        ImageView imageViewGame;

        public UserDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.game_name);
            imageViewGame = itemView.findViewById(R.id.game_image);
        }
    }
}
