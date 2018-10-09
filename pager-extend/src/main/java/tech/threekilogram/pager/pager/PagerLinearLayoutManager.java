package tech.threekilogram.pager.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

/**
 * @author Liujin 2018-10-09:9:50
 */
public class PagerLinearLayoutManager extends LinearLayoutManager {

      private static final String TAG = PagerLinearLayoutManager.class.getSimpleName();

      public PagerLinearLayoutManager ( Context context ) {

            super( context );
      }

      public PagerLinearLayoutManager (
          Context context, int orientation, boolean reverseLayout ) {

            super( context, orientation, reverseLayout );
      }

      public PagerLinearLayoutManager (
          Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes ) {

            super( context, attrs, defStyleAttr, defStyleRes );
      }

      @Override
      public LayoutParams generateDefaultLayoutParams ( ) {

            return new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            );
      }

      @Override
      public void onMeasure (
          Recycler recycler, State state, int widthSpec, int heightSpec ) {

            Log.e( TAG, "onMeasure : " + MeasureSpec.toString( widthSpec ) + " " + MeasureSpec
                .toString( heightSpec ) );
            super.onMeasure( recycler, state, widthSpec, heightSpec );
      }

      @Override
      public void onLayoutChildren (
          Recycler recycler, State state ) {

            super.onLayoutChildren( recycler, state );
      }
}
