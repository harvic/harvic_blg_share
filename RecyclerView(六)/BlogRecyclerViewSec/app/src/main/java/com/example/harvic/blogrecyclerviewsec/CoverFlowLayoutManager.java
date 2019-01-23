package com.example.harvic.blogrecyclerviewsec;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

public class CoverFlowLayoutManager extends LayoutManager {
    private int mSumDx = 0;
    private int mTotalWidth = 0;
    private int mItemWidth, mItemHeight;
    private SparseArray<Rect> mItemRects = new SparseArray<>();
    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }


    private int mIntervalWidth;

    private int mStartX;

    @Override
    public void onLayoutChildren(Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        mHasAttachedItems.clear();
        mItemRects.clear();
        detachAndScrapAttachedViews(recycler);

        //将item的位置存储起来
        View childView = recycler.getViewForPosition(0);
        measureChildWithMargins(childView, 0, 0);
        mItemWidth = getDecoratedMeasuredWidth(childView);
        mItemHeight = getDecoratedMeasuredHeight(childView);

        mIntervalWidth = getIntervalWidth();

        mStartX = getWidth() / 2 - mIntervalWidth;

        //定义水平方向的偏移量
        int offsetX = 0;

        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(mStartX + offsetX, 0, mStartX + offsetX + mItemWidth, mItemHeight);
            mItemRects.put(i, rect);
            mHasAttachedItems.put(i, false);
            offsetX += mIntervalWidth;
        }

        int visibleCount = getHorizontalSpace() / mIntervalWidth;
        Rect visibleRect = getVisibleArea();
        for (int i = 0; i < visibleCount; i++) {
            insertView(i, visibleRect, recycler, false);
        }

        //如果所有子View的宽度和没有填满RecyclerView的宽度，
        // 则将宽度设置为RecyclerView的宽度
        mTotalWidth = Math.max(offsetX, getHorizontalSpace());
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, Recycler recycler, RecyclerView.State state) {
        if (getChildCount() <= 0) {
            return dx;
        }

        int travel = dx;
        //如果滑动到最顶部
        if (mSumDx + dx < 0) {
            travel = -mSumDx;
        } else if (mSumDx + dx > getMaxOffset()) {
            //如果滑动到最底部
            travel = getMaxOffset() - mSumDx;
        }

        mSumDx += travel;

        Rect visibleRect = getVisibleArea();

        //回收越界子View
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            int position = getPosition(child);
            Rect rect = mItemRects.get(position);

            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(child, recycler);
                mHasAttachedItems.put(position, false);
            } else {
                layoutDecoratedWithMargins(child, rect.left - mSumDx, rect.top, rect.right - mSumDx, rect.bottom);
                handleChildView(child, rect.left - mStartX - mSumDx);
                mHasAttachedItems.put(position, true);
            }
        }
        //填充空白区域
        View lastView = getChildAt(getChildCount() - 1);
        View firstView = getChildAt(0);
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
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            View child = recycler.getViewForPosition(pos);
            if (firstPos) {
                addView(child, 0);
            } else {
                addView(child);
            }
            measureChildWithMargins(child, 0, 0);
            layoutDecoratedWithMargins(child, rect.left - mSumDx, rect.top, rect.right - mSumDx, rect.bottom);

            mHasAttachedItems.put(pos, true);
            handleChildView(child, rect.left - mSumDx - mStartX);
        }
    }

    /**
     * 获取可见的区域Rect
     *
     * @return
     */
    private Rect getVisibleArea() {
        Rect result = new Rect(getPaddingLeft() + mSumDx, getPaddingTop(), getWidth() - getPaddingRight() + mSumDx, getHeight() - getPaddingBottom());
        return result;
    }

    public int getIntervalWidth() {
        return mItemWidth / 2;
    }

    public int getCenterPosition() {
        int pos = (int) (mSumDx / getIntervalWidth());
        int more = (int) (mSumDx % getIntervalWidth());
        if (more > getIntervalWidth() * 0.5f) pos++;
        return pos;
    }

    /**
     * 获取第一个可见的Item位置
     * <p>Note:该Item为绘制在可见区域的第一个Item，有可能被第二个Item遮挡
     */
    public int getFirstVisiblePosition() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View view = getChildAt(0);
        int pos = getPosition(view);

        return pos;
    }


    private void handleChildView(View child, int moveX) {
        float radio = computeScale(moveX);
        float rotation = computeRotationY(moveX);

        child.setScaleX(radio);
        child.setScaleY(radio);

        child.setRotationY(rotation);
    }

    /**
     * 计算Item缩放系数
     *
     * @param x Item的偏移量
     * @return 缩放系数
     */
    private float computeScale(int x) {
        float scale = 1 - Math.abs(x * 2.0f / (8f * getIntervalWidth()));
        if (scale < 0) scale = 0;
        if (scale > 1) scale = 1;
        return scale;
    }

    /**
     * 获取最大偏移量
     */
    private int getMaxOffset() {
        return (getItemCount() - 1) * getIntervalWidth();
    }

    public double calculateDistance(int velocityX, double distance) {
        int extra = mSumDx % getIntervalWidth();
        double realDistance;
        if (velocityX > 0) {
            if (distance < getIntervalWidth()) {
                realDistance = getIntervalWidth() - extra;
            } else {
                realDistance = distance - distance % getIntervalWidth() - extra;
            }
        } else {
            if (distance < getIntervalWidth()) {
                realDistance = extra;
            } else {
                realDistance = distance - distance % getIntervalWidth() + extra;
            }
        }
        return realDistance;
    }

    /**
     * 最大Y轴旋转度数
     */
    private float M_MAX_ROTATION_Y = 30.0f;

    private float computeRotationY(int x) {
        float rotationY;
        rotationY = -M_MAX_ROTATION_Y * x / getIntervalWidth();
        if (Math.abs(rotationY) > M_MAX_ROTATION_Y) {
            if (rotationY > 0) {
                rotationY = M_MAX_ROTATION_Y;
            } else {
                rotationY = -M_MAX_ROTATION_Y;
            }
        }
        return rotationY;
    }
}
