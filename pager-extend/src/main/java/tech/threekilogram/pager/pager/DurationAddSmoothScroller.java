package tech.threekilogram.pager.pager;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * @author Liujin 2018-09-23:9:48
 */
public class DurationAddSmoothScroller extends LinearSmoothScroller {

      private int mDurationAdded;

      public DurationAddSmoothScroller ( Context context ) {

            super( context );
      }

      public void setDurationAdded ( int durationAdded ) {

            mDurationAdded = durationAdded;
      }

      public int getDurationAdded ( ) {

            return mDurationAdded;
      }

      @Override
      protected int calculateTimeForScrolling ( int dx ) {

            return super.calculateTimeForScrolling( dx ) + mDurationAdded;
      }
}
