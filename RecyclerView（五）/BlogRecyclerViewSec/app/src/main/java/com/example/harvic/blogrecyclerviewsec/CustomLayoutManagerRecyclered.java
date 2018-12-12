package com.example.harvic.blogrecyclerviewsec;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public class CustomLayoutManagerRecyclered extends LayoutManager {
    private int mSumDy = 0;
    private int mTotalHeight = 0;

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    private int mItemWidth, mItemHeight;
    private SparseArray<Rect> mItemRects = new SparseArray<>();
    ;

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
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

        //回收越界子View
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (travel > 0) {//需要回收当前屏幕，上越界的View
                if (getDecoratedBottom(child) - travel < 0) {
                    removeAndRecycleView(child, recycler);
                    continue;
                }
            } else if (travel < 0) {//回收当前屏幕，下越界的View
                if (getDecoratedTop(child) - travel > getHeight() - getPaddingBottom()) {
                    removeAndRecycleView(child, recycler);
                    continue;
                }
            }
        }

        Rect visibleRect = getVisibleArea(travel);
//布局子View阶段
        if (travel >= 0) {
            View lastView = getChildAt(getChildCount() - 1);
            int minPos = getPosition(lastView) + 1;//从最后一个View+1开始吧

            //顺序addChildView
            for (int i = minPos; i <= getItemCount() - 1; i++) {
                Rect rect = mItemRects.get(i);
                if (Rect.intersects(visibleRect, rect)) {
                    View child = recycler.getViewForPosition(i);
                    addView(child);
                    measureChildWithMargins(child, 0, 0);
                    layoutDecorated(child, rect.left, rect.top - mSumDy, rect.right, rect.bottom - mSumDy);
                } else {
                    break;
                }
            }
        } else {
            View firstView = getChildAt(0);
            int maxPos = getPosition(firstView) - 1;

            for (int i = maxPos; i >= 0; i--) {
                Rect rect = mItemRects.get(i);
                if (Rect.intersects(visibleRect, rect)) {
                    View child = recycler.getViewForPosition(i);
                    addView(child, 0);//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                    measureChildWithMargins(child, 0, 0);
                    layoutDecoratedWithMargins(child, rect.left, rect.top - mSumDy, rect.right, rect.bottom - mSumDy);
                } else {
                    break;
                }
            }
        }

        mSumDy += travel;
// 平移容器内的item
        offsetChildrenVertical(-travel);
        return travel;
    }


    /**
     * 获取可见的区域Rect
     *
     * @return
     */
    private Rect getVisibleArea(int dy) {
        Rect result = new Rect(getPaddingLeft(), getPaddingTop() + mSumDy + dy, getWidth() + getPaddingRight(), getVerticalSpace() + mSumDy + dy);
        return result;
    }
}
