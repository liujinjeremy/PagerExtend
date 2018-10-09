package tech.threekilogram.pager.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @author Liujin 2018-10-09:9:50
 */
public class MatchParentLinearLayoutManager extends LinearLayoutManager {

      public MatchParentLinearLayoutManager ( Context context ) {

            super( context );
      }

      public MatchParentLinearLayoutManager (
          Context context, int orientation, boolean reverseLayout ) {

            super( context, orientation, reverseLayout );
      }

      public MatchParentLinearLayoutManager (
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
}
