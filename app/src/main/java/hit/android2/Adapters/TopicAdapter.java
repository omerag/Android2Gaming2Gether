package hit.android2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import hit.android2.Database.ChildData;
import hit.android2.Database.ParentData;
import hit.android2.R;

public class TopicAdapter extends ExpandableRecyclerViewAdapter<TopicAdapter.ParentViewHolder, TopicAdapter.ChildViewHolders> {

    public Context context;

    public TopicAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.context = context;
    }


    @Override
    public ParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_parent,parent,false);

        return new ParentViewHolder(view);
    }

    @Override
    public ChildViewHolders onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_child,parent,false);

        return new ChildViewHolders(view);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolders holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ChildData childData = ((ParentData)group).getItems().get(childIndex);
        holder.setChildText(childData.getName());
        holder.textView_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Selected : " + childData.getName(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBindGroupViewHolder(ParentViewHolder holder, int flatPosition, ExpandableGroup group) {

        holder.setGroupName(group);

    }

    public class ParentViewHolder extends GroupViewHolder {

        public TextView textView_parent;
        public LinearLayout commentLayout;

        public ParentViewHolder(View itemView) {
            super(itemView);
            textView_parent = itemView.findViewById(R.id.tv_parent_item_topic_name);
            commentLayout = itemView.findViewById(R.id.parent_item_comment_layout);
        }

        @Override
        public void expand() {
            super.expand();
            textView_parent.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down,0);
            commentLayout.setVisibility(View.GONE);
        }

        @Override
        public void collapse() {
            super.collapse();
            textView_parent.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            commentLayout.setVisibility(View.VISIBLE);

        }

        public void setGroupName(ExpandableGroup groupName){
            textView_parent.setText(groupName.getTitle());
        }
    }

    public class ChildViewHolders extends ChildViewHolder {

        public TextView textView_child;

        public ChildViewHolders(View itemView) {
            super(itemView);
            textView_child = itemView.findViewById(R.id.tv_child_item_massage);
        }

        public void setChildText(String name){
            textView_child.setText(name);
        }
    }



}