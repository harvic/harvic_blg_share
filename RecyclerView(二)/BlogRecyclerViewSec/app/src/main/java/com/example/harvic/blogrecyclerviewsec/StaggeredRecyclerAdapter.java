package com.example.harvic.blogrecyclerviewsec;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class StaggeredRecyclerAdapter extends Adapter<ViewHolder> {

    private Context mContext;
    private ArrayList<String> mDatas;
    private ArrayList<Integer> mHeights = new ArrayList<>();

    public StaggeredRecyclerAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;

        if (mDatas.size() > 0) {
            for (int i = 0; i < mDatas.size(); i++) {
                mHeights.add(getRandomHeight());
            }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new NormalHolder(inflater.inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.mTV.setText(mDatas.get(position));

        ViewGroup.LayoutParams lp = normalHolder.mTV.getLayoutParams();
        lp.height = mHeights.get(position);
        normalHolder.mTV.setLayoutParams(lp);
    }

    private int getRandomHeight() {
        int randomHeight = 0;
        do {
            randomHeight = (int) (Math.random() * 300);
        } while (randomHeight == 0);
        return randomHeight;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends ViewHolder {
        public TextView mTV;

        public NormalHolder(View itemView) {
            super(itemView);

            mTV = (TextView) itemView.findViewById(R.id.item_tv);
            mTV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTV.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
