/*
Vitabu is an Open Source application available under the Apache (Version 2.0) License.

Copyright 2019 Arseniy Kouzmenkov, Owen Randall, Ayooluwa Oladosu, Tristan Carlson, Jacob Paton,
Katherine Richards

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * This file handles the activity of viewing a book from browse books. The user is then presented with
 * the option of viewing the Owner's profile, viewing a Goodreads page for the book and requesting to
 * borrow this book.
 *
 * References: "Android adding RecyclerView Swipe to Delete and Undo" by Ravi Tamada, androidhive
 * https://www.androidhive.info/2017/09/android-recyclerview-swipe-delete-undo-using-itemtouchhelper/
 *
 * Author: Katherine Richards
 * Version: 1.0
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * This class extends from the class ItemTouchHelper.SimpleCallback to add swipe functionality
 * to the recycler view items displayed in acceptBookRequestActivity.
 *
 * @author Katherine Richards
 * @version 1.0
 * @see acceptBookRequestActivity
 */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    /**
     * Called when ItemTouchHelper wants to move the dragged item from its old position to the new position.
     *
     * @param recyclerView the recycler view that the ItemTouchHelper is bound to.
     * @param viewHolder the view that is being dragged by the user.
     * @param target the view that the current item is being dragged over.
     * @return true if the viewHolder has been moved to the adapter position of the target.
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    /**
     * This method is called when the viewHolder swiped/dragged by the ItemTouchHelper is changed.
     *
     * @param viewHolder the view that is being dragged by the user.
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((acceptBookRequestsRecyclerViewAdapter.ViewHolder) viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    /**
     * This method is called by the recycler view's onDraw callback method. Dictates how to draw
     * the foreground of the swiped item.
     *
     * @param c the canvas that the recycler view is drawing on.
     * @param recyclerView the recycler view that the ItemTouchHelper is bound to.
     * @param viewHolder the view that is being dragged by the user.
     * @param dX measurement of horizontal displacement caused by the user.
     * @param dY measurement of vertical displacement caused by the user.
     * @param actionState
     * @param isCurrentlyActive true if displacement is being caused by the user, false if the item
     *                          is animating back to origin.
     */
    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((acceptBookRequestsRecyclerViewAdapter.ViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    /**
     * This method is called when the user's interaction with the item inside the recycler view is
     * over and the animation is complete.
     *
     * @param recyclerView the recycler view that the ItemTouchHelper is bound to.
     * @param viewHolder the view that is being dragged by the user.
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((acceptBookRequestsRecyclerViewAdapter.ViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    /**
     * This method is called by the recycler view's onDraw callback method. Dictates how to draw
     * the foreground of the swiped item.
     *
     * @param c the canvas that the recycler view is drawing on.
     * @param recyclerView the recycler view that the ItemTouchHelper is bound to.
     * @param viewHolder the view that is being dragged by the user.
     * @param dX measurement of horizontal displacement caused by the user.
     * @param dY measurement of vertical displacement caused by the user.
     * @param actionState
     * @param isCurrentlyActive true if displacement is being caused by the user, false if the item
     *                          is animating back to origin.
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((acceptBookRequestsRecyclerViewAdapter.ViewHolder) viewHolder).viewForeground;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    /**
     * Called when a view is swiped by the user.
     *
     * @param viewHolder the view that is being dragged by the user.
     * @param direction the direction in which the view is swiped.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    /**
     * This method converts a set of flags to directions to determine the start and end position
     * of the swipe animation.
     *
     * @param flags movement flag value.
     * @param layoutDirection the layout direction of the recycler view.
     * @return int which includes absolute direction values.
     */
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    /**
     * Interface to be implemented by parent activity
     * @see acceptBookRequestActivity
     */
    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}

