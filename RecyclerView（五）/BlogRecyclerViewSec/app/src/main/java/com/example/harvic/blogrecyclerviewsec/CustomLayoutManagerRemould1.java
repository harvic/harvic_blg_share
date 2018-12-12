package com.example.harvic.blogrecyclerviewsec;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

public class CustomLayoutManagerRemould1 extends LayoutManager {
    private int mSumDy = 0;
    private int mTotalHeight = 0;

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    private int mItemWidth, mItemHeight;
    private SparseArray<Rect> mItemRects = new SparseArray<>();

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        mItemRects.clear();

        detachAndScrapAttachedViews(recycler);

        //将item的位置存储起来
        View childView = recycler.getViewForPosition(0);
        measureChildWithMargins(childView, 0, 0);
        mItemWidth = getDecoratedMeasuredWidth(childView);
        mItemHeight = getDecoratedMeasuredHeight(childView);

        int visibleCount = getVerticalSpace() / mItemHeight;


        //定义竖直方向的偏移量
        int offsetY = 0;

        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(0, offsetY, mItemWidth, offsetY + mItemHeight);
            mItemRects.put(i, rect);
            offsetY += mItemHeight;
        }


        for (int i = 0; i < visibleCount; i++) {
            Rect rect = mItemRects.get(i);
            View view = recycler.getViewForPosition(i);
            addView(view);
            //addView后一定要measure，先measure再layout
            measureChildWithMargins(view, 0, 0);
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
        }

        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        mTotalHeight = Math.max(offsetY, getVerticalSpace());
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() <= 0) {
            return dy;
        }

        int travel = dy;
//如果滑动到最顶部
        if (mSumDy + dy < 0) {
            travel = -mSumDy;
        } else if (mSumDy + dy > mTotalHeight - getVerticalSpace()) {
            //如果滑动到最底部
            travel = mTotalHeight - getVerticalSpace() - mSumDy;
        }

        mSumDy += travel;

        Rect visibleRect = getVisibleArea();

        //回收越界子View
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            int position = getPosition(child);
            Rect rect = mItemRects.get(position);
            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(child, recycler);
            }
        }

        View lastView = getChildAt(getChildCount() - 1);
        View firstView = getChildAt(0);
        detachAndScrapAttachedViews(recycler);

        if (travel >= 0) {
            int minPos = getPosition(firstView);
            for (int i = minPos; i < getItemCount(); i++) {
                insertView(i, visibleRect, recycler, false);
            }
        } else {
            int maxPos = getPosition(lastView);
            for (int i = maxPos; i >= 0; i--) {
                insertView(i, visibleRect, recycler, true);
            }
        }
        return travel;
    }

    private void insertView(int pos, Rect visibleRect, Recycler recycler, boolean firstPos) {
        Rect rect = mItemRects.get(pos);
        if (Rect.intersects(visibleRect, rect)) {
            View child = recycler.getViewForPosition(pos);
            if (firstPos) {
                addView(child, 0);
            } else {
                addView(child);
            }
            measureChildWithMargins(child, 0, 0);
            layoutDecoratedWithMargins(child, rect.left, rect.top - mSumDy, rect.right, rect.bottom - mSumDy);

            //在布局item后，修改每个item的旋转度数
            child.setRotationY(child.getRotationY() + 1);
        }
    }

    /**
     * 获取可见的区域Rect
     *
     * @return
     */
    private Rect getVisibleArea() {
        Rect result = new Rect(getPaddingLeft(), getPaddingTop() + mSumDy, getWidth() + getPaddingRight(), getVerticalSpace() + mSumDy);
        return result;
    }

}
