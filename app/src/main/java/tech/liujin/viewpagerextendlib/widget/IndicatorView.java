package tech.liujin.viewpagerextendlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LiuJin on 2017-12-31:14:07
 */

public class IndicatorView extends View {

      private int   mHeight;
      private int   mWidth;
      private Paint mPaint;
      private int   mCount;
      private int   mItemWidth;
      private int   mItemIndex;
      private float mItemOffset;

      public IndicatorView ( Context context ) {

            this( context, null );
      }

      public IndicatorView ( Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public IndicatorView ( Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init( context );
      }

      private void init ( Context context ) {

            mPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
            mPaint.setStyle( Paint.Style.FILL );
            mPaint.setColor( Color.parseColor( "#b22222" ) );
      }

      @Override
      protected void onSizeChanged ( int w, int h, int oldw, int oldh ) {

            super.onSizeChanged( w, h, oldw, oldh );
            mHeight = h;
            mWidth = w;
            mPaint.setStrokeWidth( h );
            mItemWidth = w / mCount;
      }

      public void setCount ( int count ) {

            mCount = count;
            if( mWidth != 0 ) {
                  mItemWidth = mWidth / count;
            }
      }

      public void setXOff ( int itemIndex, float itemOffset ) {

            mItemIndex = itemIndex;
            mItemOffset = itemOffset;
            invalidate();
      }

      @Override
      protected void onDraw ( Canvas canvas ) {

            super.onDraw( canvas );

            float startX = mItemIndex * mItemWidth + mItemOffset * mItemWidth;
            float stopX = ( mItemIndex + 1 ) * mItemWidth + mItemOffset * mItemWidth;

            canvas.drawLine(
                startX,
                mHeight / 2,
                stopX,
                mHeight / 2,
                mPaint
            );
      }
}
