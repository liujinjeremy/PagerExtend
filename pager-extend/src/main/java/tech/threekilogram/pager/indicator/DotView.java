package tech.threekilogram.pager.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import tech.threekilogram.pager.banner.ViewPagerBanner;

/**
 * @author LiuJin
 * @date 2017-12-25
 *     实现一个小圆点
 */
public class DotView extends View {

      private int   mColorSelected;
      private int   mColorNormal;
      private Paint mPaint;

      private int       mDotMargin;
      private int       mDotSize;
      private int       mDotCount;
      private boolean[] mSelect;

      private OnPageChangeListener mOnPageChangeListener;

      public DotView ( Context context ) {

            this( context, null, 0 );
      }

      public DotView (
          Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public DotView ( Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init( context );
      }

      private void init ( Context context ) {

            mPaint = new Paint();
            mPaint.setAntiAlias( true );
            mColorNormal = Color.LTGRAY;
            mColorSelected = Color.WHITE;

            mDotSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                getResources().getDisplayMetrics()
            );
            mDotMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                getResources().getDisplayMetrics()
            );
      }

      public void setColor ( int colorSelected, int colorNormal ) {

            mColorSelected = colorSelected;
            mColorNormal = colorNormal;
      }

      public void setColorNormal ( int colorNormal ) {

            mColorNormal = colorNormal;
      }

      public void setColorSelected ( int colorSelected ) {

            mColorSelected = colorSelected;
      }

      public void setDotMargin ( int dotMargin ) {

            mDotMargin = dotMargin;
            requestLayout();
      }

      public void setDotSize ( int dotSize ) {

            mDotSize = dotSize;
            requestLayout();
      }

      public void setDotCount ( int dotCount ) {

            mDotCount = dotCount;
            boolean[] temp = new boolean[ mDotCount ];
            if( mSelect != null ) {
                  System.arraycopy( mSelect, 0, temp, 0, mSelect.length );
            }
            mSelect = temp;
            requestLayout();
      }

      public void setSelected ( int index ) {

            for( int i = 0; i < mSelect.length; i++ ) {
                  mSelect[ i ] = false;
            }
            mSelect[ index ] = true;

            invalidate();
      }

      public void setupWithBanner (
          final ViewPagerBanner banner,
          int gravity, int margin ) {

            setDotCount( banner.getItemCount() );
            setSelected( banner.getCurrentItem() );

            FrameLayout.LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity = gravity;
            params.leftMargin = margin;
            params.topMargin = margin;
            params.rightMargin = margin;
            params.bottomMargin = margin;

            banner.addView(
                this,
                params
            );

            if( mOnPageChangeListener != null ) {
                  banner.removeOnPageChangeListener( mOnPageChangeListener );
            }

            mOnPageChangeListener = new OnPageChangeListener() {

                  @Override
                  public void onPageScrolled (
                      int position, float positionOffset, int positionOffsetPixels ) { }

                  @Override
                  public void onPageSelected ( int position ) {

                        setSelected( position );
                  }

                  @Override
                  public void onPageScrollStateChanged ( int state ) { }
            };
            banner.addOnPageChangeListener( mOnPageChangeListener );
      }

      @Override
      protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec ) {

            int widthMode = MeasureSpec.getMode( widthMeasureSpec );
            int widthSize = MeasureSpec.getSize( widthMeasureSpec );
            int heightMode = MeasureSpec.getMode( heightMeasureSpec );
            int heightSize = MeasureSpec.getSize( heightMeasureSpec );

            int finalWidth = 0;
            int finalHeight = 0;
            if( widthMode == MeasureSpec.EXACTLY ) {
                  finalWidth = widthSize;
            } else if( widthMode == MeasureSpec.AT_MOST ) {
                  int minContentWidth = ( mDotSize + mDotMargin ) * mDotCount - mDotMargin;
                  finalWidth = Math.min( ( minContentWidth ), widthSize );
            } else {
                  finalWidth = ( mDotSize + mDotMargin ) * mDotCount - mDotMargin;
            }
            if( heightMode == MeasureSpec.EXACTLY ) {
                  finalHeight = heightSize;
            } else if( widthMode == MeasureSpec.AT_MOST ) {
                  finalHeight = Math.min( ( mDotSize ), heightSize );
            } else {
                  finalHeight = mDotSize;
            }

            setMeasuredDimension( finalWidth, finalHeight );
      }

      @Override
      protected void onDraw ( Canvas canvas ) {

            super.onDraw( canvas );

            int radius = mDotSize / 2 - 1;
            int dotsWidth = ( mDotSize + mDotMargin ) * mDotCount - mDotMargin;
            int ry = getHeight() / 2;
            int startX = ( getWidth() - dotsWidth ) / 2;

            for( int i = 0; i < mDotCount; i++ ) {

                  if( mSelect[ i ] ) {
                        mPaint.setColor( mColorSelected );
                  } else {
                        mPaint.setColor( mColorNormal );
                  }

                  canvas.drawCircle(
                      radius + startX + ( i * ( mDotSize + mDotMargin ) ),
                      ry,
                      radius,
                      mPaint
                  );
            }
      }
}
