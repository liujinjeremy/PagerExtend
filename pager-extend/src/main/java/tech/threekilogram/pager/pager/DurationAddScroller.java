package tech.threekilogram.pager.pager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 辅助类,可以增加时间滚动时间的Scroller
 *
 * @author liujin
 */
@SuppressWarnings("WeakerAccess")
public class DurationAddScroller extends Scroller {

      private int mDurationAdded = 0;

      public DurationAddScroller ( Context context ) {

            super( context );
      }

      public DurationAddScroller ( Context context, Interpolator interpolator ) {

            super( context, interpolator );
      }

      public DurationAddScroller (
          Context context, Interpolator interpolator, boolean flywheel ) {

            super( context, interpolator, flywheel );
      }

      /**
       * @param durationAdded 设置Scroller滚动时增加的时间,单位ms
       */
      public void setDurationAdded ( int durationAdded ) {

            mDurationAdded = durationAdded;
      }

      public int getDurationAdded ( ) {

            return mDurationAdded;
      }

      @Override
      public void startScroll ( int startX, int startY, int dx, int dy, int duration ) {

            super.startScroll( startX, startY, dx, dy, duration + mDurationAdded );
      }
}
