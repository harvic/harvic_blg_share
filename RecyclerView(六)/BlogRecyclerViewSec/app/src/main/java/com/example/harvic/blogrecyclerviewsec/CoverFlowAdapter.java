package com.example.harvic.blogrecyclerviewsec;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class CoverFlowAdapter extends Adapter<ViewHolder> {

    private Context mContext;
    private ArrayList<String> mDatas;
    private int mCreatedHolder=0;
    private int[] mPics = {R.mipmap.item1,R.mipmap.item2,R.mipmap.item3,R.mipmap.item4,
            R.mipmap.item5,R.mipmap.item6};
    public CoverFlowAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mCreatedHolder++;
        Log.d("qijian", "onCreateViewHolder num:"+mCreatedHolder);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new NormalHolder(inflater.inflate(R.layout.item_coverflow, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("qijian", "onBindViewHolder");
        NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.mTV.setText(mDatas.get(position));
        normalHolder.mImg.setImageDrawable(mContext.getResources().getDrawable(mPics[position%mPics.length]));
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends ViewHolder {
        public TextView mTV;
        public ImageView mImg;

        public NormalHolder(View itemView) {
            super(itemView);

            mTV = (TextView) itemView.findViewById(R.id.text);
            mTV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTV.getText(), Toast.LENGTH_SHORT).show();
                }
            });

            mImg = (ImageView)itemView.findViewById(R.id.img);
            mImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTV.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
