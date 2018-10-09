package com.example.harvic.blogrecyclerviewsec;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Bitmap mBmp, mMedalBmp;

    public LinearItemDecoration(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        BitmapFactory.Options options = new BitmapFactory.Options();
//    options.inSampleSize = 2;
        mBmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon, options);
        mMedalBmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.xunzhang);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //画圆
//        int childCount = parent.getChildCount();
//        RecyclerView.LayoutManager manager = parent.getLayoutManager();
//        for (int i = 0; i < childCount; i++) {
//            View child = parent.getChildAt(i);
//            //动态获取outRect的left值
//            int left = manager.getLeftDecorationWidth(child);
//            int cx = left / 2;
//            int cy = child.getTop() + child.getHeight() / 2;
//            c.drawCircle(cx, cy, 20, mPaint);
//        }


        //画icon图形
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = parent.getChildAt(i);
//            c.drawBitmap(mBmp,0,child.getTop(), mPaint);
//        }


    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //画勋章
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);
            int left = manager.getLeftDecorationWidth(child);
            if (index % 5 == 0) {
                c.drawBitmap(mMedalBmp, left - mMedalBmp.getWidth() / 2, child.getTop(), mPaint);
            }
        }
        View temp = parent.getChildAt(0);
        LinearGradient gradient = new LinearGradient(parent.getWidth() / 2, 0, parent.getWidth() / 2, temp.getHeight() * 3,
                0xff0000ff, 0x000000ff, Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        c.drawRect(0, 0, parent.getWidth(), temp.getHeight() * 3, mPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = 200;
        outRect.bottom = 1;
    }
}
