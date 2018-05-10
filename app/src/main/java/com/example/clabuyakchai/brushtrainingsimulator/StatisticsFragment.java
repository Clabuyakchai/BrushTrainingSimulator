package com.example.clabuyakchai.brushtrainingsimulator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clabuyakchai.brushtrainingsimulator.database.DBQuery;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserStatistics;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Clabuyakchai on 07.05.2018.
 */

public class StatisticsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private StatisticsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerView();
    }

    private void setRecyclerView(){
        DBQuery dbQuery = new DBQuery(getActivity());
        List<UserStatistics> list = dbQuery.getStatisticsUsername(DBQuery.QUERY_USERNAME);
        if(list != null){
            if(mAdapter == null){
                mAdapter = new StatisticsAdapter(list);
            } else {
                mAdapter.setList(list);
            }
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public class StatisticsHolder extends RecyclerView.ViewHolder{

        private UserStatistics mStatistics;

        private ImageView mImageView;
        private TextView mDesription;
        private TextView mCounter;
        private TextView mData;

        public StatisticsHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.level);
            mDesription = itemView.findViewById(R.id.descriptionListItem);
            mCounter = itemView.findViewById(R.id.counterListItem);
            mData = itemView.findViewById(R.id.dataListItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), mStatistics.getDescription(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bind(UserStatistics statistics){
            this.mStatistics = statistics;
            mDesription.setText(statistics.getDescription().toString());
            mCounter.setText(String.valueOf(mStatistics.getCounter()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mData.setText(dateFormat.format(mStatistics.getData()));
            mImageView.setImageResource(setImage(mStatistics.getCounter()));
        }

        private int setImage(int count){
            if(count >= 100){
                return R.drawable.icon_level3;
            } else if (count >= 50) {
                return R.drawable.icon_level2;
            } else {
                return R.drawable.icon_level1;
            }
        }
    }

    public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsHolder>{

        private List<UserStatistics> list;

        public StatisticsAdapter(List<UserStatistics> list) {
            this.list = list;
        }

        @Override
        public StatisticsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item, parent, false);

            return new StatisticsHolder(view);
        }

        @Override
        public void onBindViewHolder(StatisticsHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<UserStatistics> list) {
            this.list = list;
        }
    }

    public static StatisticsFragment newInstance(){
        return new StatisticsFragment();
    }
}
