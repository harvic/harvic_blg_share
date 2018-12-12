package com.example.harvic.blogrecyclerviewsec;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class DiffItemAdapter extends Adapter<ViewHolder> {

    private Context mContext;
    private ArrayList<String> mDatas;

    public static enum ITEM_TYPE {
        ITEM_TYPE_SECTION,
        ITEM_TYPE_ITEM
    }

    private int M_SECTION_ITEM_NUM = 10;

    public DiffItemAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == ITEM_TYPE.ITEM_TYPE_ITEM.ordinal()) {
            return new NormalHolder(inflater.inflate(R.layout.item_layout, parent, false));
        }
        return new SectionHolder(inflater.inflate(R.layout.item_section_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionHolder) {
            SectionHolder sectionHolder = (SectionHolder) holder;
            sectionHolder.mSectionTv.setText("第 " + position / M_SECTION_ITEM_NUM + " 组");
        } else if (holder instanceof NormalHolder) {
            NormalHolder normalHolder = (NormalHolder) holder;
            normalHolder.mTV.setText(mDatas.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % M_SECTION_ITEM_NUM == 0) {
            return ITEM_TYPE.ITEM_TYPE_SECTION.ordinal();
        }
        return ITEM_TYPE.ITEM_TYPE_ITEM.ordinal();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {
        public TextView mTV;

        public NormalHolder(View itemView) {
            super(itemView);

            mTV = (TextView) itemView.findViewById(R.id.item_tv);
            mTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTV.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public class SectionHolder extends RecyclerView.ViewHolder {

        public TextView mSectionTv;

        public SectionHolder(View itemView) {
            super(itemView);
            mSectionTv = (TextView) itemView.findViewById(R.id.item_section_tv);
        }
    }
}
