package hit.android2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MessagesRootFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MessagesPagerAdapter messagesPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages_root_fragment, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        messagesPagerAdapter = new MessagesPagerAdapter(getActivity().getSupportFragmentManager(),1);
        viewPager.setAdapter(messagesPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    public class MessagesPagerAdapter extends FragmentPagerAdapter
    {
        public MessagesPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.d("insideFragment","create");

            switch (position)
            {
                case 0: return new MessagesFragment();
                case 1: return new GroupMessagesFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0: return "Chats";
                case 1: return "Groups";
            }
            return null;
        }
    }
}
