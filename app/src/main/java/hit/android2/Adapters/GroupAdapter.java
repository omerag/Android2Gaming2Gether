package hit.android2.Adapters;

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

import hit.android2.Database.Model.GroupData;
import hit.android2.R;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupDataViewHolder> {

    private Context context;
    private List<GroupData> groupDataList;
    private GroupAdapterListener listener;

    public GroupAdapter(Context context, List<GroupData> groupDataList) {
        this.context = context;
        this.groupDataList = groupDataList;
    }

    public void setGroupDataList(List<GroupData> groupDataList) {
        this.groupDataList = groupDataList;
    }

    public interface GroupAdapterListener{
        void onClick(View view, int position);
    }

    public void setListener(GroupAdapterListener listener){
        this.listener = listener;

    }

    @NonNull
    @Override
    public GroupDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.messages_card_layout,parent,false);
        GroupDataViewHolder viewHolder = new GroupDataViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GroupDataViewHolder holder, int position) {

        Glide.with(context).load(groupDataList.get(position).getImage_URL()).into(holder.groupPicture);

        holder.groupName.setText(groupDataList.get(position).getGroup_name());
        //manager.GetLastMessage(userDataList.get(position).getKey(),holder.textViewLastMessage);

    }

    @Override
    public int getItemCount() {
        if(groupDataList != null){
            return groupDataList.size();
        }
        return 0;
    }


    public class GroupDataViewHolder extends RecyclerView.ViewHolder{

        TextView groupName;
        TextView lastMessage;
        ImageView groupPicture;

        public GroupDataViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.user_name_tv);
            lastMessage = itemView.findViewById(R.id.last_message_tv);
            groupPicture = itemView.findViewById(R.id.user_image);


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
}
