package com.dr.frappe.activity.expense;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dr.frappe.R;

/**
 * This class is used to create the item decorations for introducing spacing between items,
 * a divider etc. because the RecyclerView does not contain all those things (unlike a ListView)
 * Created by rohitman on 11/15/2015.
 */
public class ExpenseRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;

    public ExpenseRecyclerItemDecoration(Context context) {
        this.context = context;
    }

    /**
     * getItemOffsets decides the spacing around the item
     * inspired by: http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // set a top and bottom for all elements except the first and the last
        if ((parent.getChildAdapterPosition(view) == 0) ||
                (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)) {
            outRect.bottom = (int)context.getResources().getDimension(R.dimen.expense_recyclerview_bottom);
            outRect.top = (int)context.getResources().getDimension(R.dimen.expense_recyclerview_top);
            outRect.left = (int)context.getResources().getDimension(R.dimen.expense_recyclerview_right);
            outRect.right = (int)context.getResources().getDimension(R.dimen.expense_recyclerview_left);

            Log.i(this.getClass().getName(), "Building item decoration for #" + parent.getChildAdapterPosition(view) +
                "; Dimensions: " + outRect.left + "/" + outRect.top + "/" + outRect.right + "/" + outRect.bottom);
        }
    }

    /**
     * onDraw is used to control the item decoration of each item in the recyclerview and draw a
     * separator between the items. this method is a very expensive call, so control what happens
     * through it
     * inspired by: http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        // Get the Divider to use
        Drawable divider = context.getDrawable(R.drawable.expense_recyclerview_divider);

        int left = parent.getPaddingLeft() + 50;
        int right = parent.getWidth() - parent.getPaddingRight() - 50;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
