package com.example.harvic.blogrecyclerviewsec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class StaggeredActivity extends AppCompatActivity {
    private ArrayList<String> mDatas = new ArrayList<>();

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_staggered);

    generateDatas();
    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.stagger_recycler_view);
    //瀑布流布局
    StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(staggeredManager);

    StaggeredRecyclerAdapter adapter = new StaggeredRecyclerAdapter(this, mDatas);
    mRecyclerView.setAdapter(adapter);
}

    private void generateDatas() {
        for (int i = 0; i < 200; i++) {
            mDatas.add("第 " + i + " 个item");
        }
    }
}