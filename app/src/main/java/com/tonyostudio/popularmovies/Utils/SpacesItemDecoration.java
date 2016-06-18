package com.tonyostudio.popularmovies.Utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tonyofrancis on 5/22/16.
 * Class used to specify the amount of space
 * around each item inside of a RecyclerView
 */

public final class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mLeft,mTop,mRight,mBottom;

    public SpacesItemDecoration(int space) {
      this(space,space,space,space);
    }

    public SpacesItemDecoration(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = mLeft;
        outRect.right = mRight;
        outRect.bottom = mBottom;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = mTop;
        } else {
            outRect.top = 0;
        }
    }
}