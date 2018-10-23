package tech.threekilogram.pager.image;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Liujin 2018-10-23:15:19
 */
public class ScaleImageViewPager extends ViewPager {

      public ScaleImageViewPager ( @NonNull Context context ) {

            this( context, null );
      }

      public ScaleImageViewPager (
          @NonNull Context context,
          @Nullable AttributeSet attrs ) {

            super( context, attrs );
            init();
      }

      private void init ( ) {

      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent ev ) {

            boolean b = super.dispatchTouchEvent( ev );
            int currentItem = getCurrentItem();

            return false;
      }
}
