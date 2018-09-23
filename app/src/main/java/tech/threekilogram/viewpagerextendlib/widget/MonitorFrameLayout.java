package tech.threekilogram.viewpagerextendlib.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * @author Liujin 2018-09-16:9:29
 */
public class MonitorFrameLayout extends FrameLayout {

      private static final String TAG = MonitorFrameLayout.class.getSimpleName();
      private int mCount;

      public MonitorFrameLayout ( @NonNull Context context ) {

            super( context );
      }

      public MonitorFrameLayout (
          @NonNull Context context,
          @Nullable AttributeSet attrs ) {

            super( context, attrs );
      }

      public MonitorFrameLayout (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
      }

      @Override
      protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec ) {

            super.onMeasure( widthMeasureSpec, heightMeasureSpec );
            Log.e( TAG, "onMeasure : " + ++mCount );
      }

      @Override
      protected void onLayout ( boolean changed, int left, int top, int right, int bottom ) {

            super.onLayout( changed, left, top, right, bottom );
            Log.e( TAG, "onLayout : " + mCount );
      }
}
