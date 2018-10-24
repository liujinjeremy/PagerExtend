package tech.threekilogram.pager.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import tech.threekilogram.pager.adapter.BaseTypePagerAdapter;

/**
 * @author Liujin 2018-10-23:15:19
 */
public class ScaleImageViewPager extends ViewPager {

      private static final String TAG = ScaleImageViewPager.class.getSimpleName();

      private int mSate = ViewPager.SCROLL_STATE_IDLE;
      private int mPositionOffsetPixels;
      private int mScrollPosition;

      /**
       * 记录是否触发缩放事件
       */
      private boolean     isScaleHandled;
      /**
       * 记录上一次的传递给viewpager的event
       */
      private MotionEvent mLastMotionEvent;

      /**
       * recode move distance
       */
      private float mDownX;
      private float mDownY;
      private float mLastX;
      private float mLastY;
      private float mDx;
      private float mDy;
      /**
       * 记录上一次按下时间,用于双击判断
       */
      private long  mLastDownEventTime = 0;

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

            addOnPageChangeListener( new ScrollSateListener() );
      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent ev ) {

            if( mSate == ViewPager.SCROLL_STATE_SETTLING ) {
                  return false;
            }

            return super.dispatchTouchEvent( ev );
      }

      @Override
      public boolean onTouchEvent ( MotionEvent ev ) {

            BaseImageAdapter adapter = getAdapter();
            if( adapter == null ) {
                  return super.onTouchEvent( ev );
            }

            int currentItem = getCurrentItem();
            int viewType = adapter.getViewType( currentItem );
            if( viewType == BaseImageAdapter.TYPE_SCALE_IMAGE ) {
                  ScaleImageView view = adapter.getScaleImageViewAt( currentItem );

                  /* 多指缩放 */
                  if( ev.getPointerCount() > 1 ) {
                        view.handleScaleGesture( ev );
                        isScaleHandled = true;
                        return true;
                  }

                  switch( ev.getAction() ) {

                        case MotionEvent.ACTION_DOWN:

                              mDownX = mLastX = ev.getX();
                              mDownY = mLastY = ev.getY();
                              if( isDoubleTap() ) {
                                    view.reset();
                              }
                              mLastMotionEvent = MotionEvent.obtain( ev );
                              break;
                        case MotionEvent.ACTION_MOVE:

                              if( isScaleHandled ) {
                                    return true;
                              }

                              float x = ev.getX();
                              float y = ev.getY();
                              mDx = x - mLastX;
                              mDy = y - mLastY;
                              mLastX = x;
                              mLastY = y;

                              float movedX = scaleItemMovedX( view );
                              float dx = mDx - movedX;

                              MotionEvent event = createMoveMotionEvent(
                                  mLastMotionEvent, dx, mDy, ev );
                              mLastMotionEvent.recycle();
                              mLastMotionEvent = event;
                              return super.onTouchEvent( event );

                        case MotionEvent.ACTION_UP:

                              mDownX = mDownY = mLastX = mLastY = mDx = mDy = 0;
                              isScaleHandled = false;
                              MotionEvent upMotionEvent = createUpMotionEvent(
                                  mLastMotionEvent, ev );
                              mLastMotionEvent.recycle();
                              mLastMotionEvent = null;
                              return super.onTouchEvent( upMotionEvent );

                        default:
                              break;
                  }
            }

            return super.onTouchEvent( ev );
      }

      private MotionEvent createUpMotionEvent (
          MotionEvent lastMotionEvent, MotionEvent upEvent ) {

            return MotionEvent.obtain(
                lastMotionEvent.getDownTime(),
                upEvent.getEventTime(),
                upEvent.getAction(),
                lastMotionEvent.getX(),
                lastMotionEvent.getY(),
                upEvent.getMetaState()
            );
      }

      private MotionEvent createMoveMotionEvent (
          MotionEvent lastMotionEvent, float dx, float dy, MotionEvent currentMotionEvent ) {

            return MotionEvent.obtain(
                lastMotionEvent.getDownTime(),
                currentMotionEvent.getEventTime(),
                currentMotionEvent.getAction(),
                lastMotionEvent.getX() + dx,
                lastMotionEvent.getY() + dy,
                currentMotionEvent.getMetaState()
            );
      }

      private float scaleItemMovedX ( ScaleImageView imageView ) {

            RectF rect = imageView.getDrawableRect();
            if( rect.left < 0 && mDx > 0 ) {

                  float dx = mDx;
                  if( rect.left + mDx > 0 ) {
                        dx = -rect.left;
                  }
                  imageView.setTranslateX( imageView.getTranslateX() + dx );
                  return dx;
            }

            int width = imageView.getWidth();

            if( rect.right > width && mDx < 0 ) {

                  float dx = mDx;
                  if( rect.right + mDx < width ) {
                        dx = width - rect.right;
                  }
                  imageView.setTranslateX( imageView.getTranslateX() + dx );
                  return dx;
            }

            return 0;
      }

      /**
       * 判断是否是双击
       *
       * @return true : 双击
       */
      private boolean isDoubleTap ( ) {

            long currentTime = System.currentTimeMillis();
            final int i = 300;
            if( currentTime - mLastDownEventTime < i ) {
                  return true;
            }
            mLastDownEventTime = currentTime;
            return false;
      }

      public void setAdapter ( @Nullable BaseImageAdapter adapter ) {

            super.setAdapter( adapter );
      }

      @Nullable
      @Override
      public BaseImageAdapter getAdapter ( ) {

            return (BaseImageAdapter) super.getAdapter();
      }

      /**
       * 辅助创建界面
       */
      public static abstract class BaseImageAdapter extends BaseTypePagerAdapter {

            public static final int TYPE_SCALE_IMAGE = 0;

            @Override
            protected int getViewType ( int position ) {

                  return TYPE_SCALE_IMAGE;
            }

            /**
             * 如果{@link #getViewType(int)}为{@link #TYPE_SCALE_IMAGE},那么需要返回一个{@link
             * ScaleImageView}操作
             *
             * @param position 布局位置
             *
             * @return ScaleImageView
             */
            protected abstract ScaleImageView getScaleImageViewAt ( int position );
      }

      /**
       * {@link BaseImageAdapter}的简单实现版本
       */
      public static abstract class SimpleImageAdapter extends BaseImageAdapter {

            @Override
            protected ScaleImageView getScaleImageViewAt ( int position ) {

                  return (ScaleImageView) getItemView( position );
            }

            @Override
            protected Object getData ( int position, int type ) {

                  return getBitmapAt( position );
            }

            /**
             * 获取该位置的bitmap
             *
             * @param position 位置
             *
             * @return bitmap
             */
            protected abstract Bitmap getBitmapAt ( int position );

            @Override
            protected View getView ( ViewGroup container, int position, int type ) {

                  return new ScaleImageView( container.getContext() );
            }

            @Override
            protected void bindData ( int position, Object data, View view, int type ) {

                  ( (ScaleImageView) view ).setImageBitmap( (Bitmap) data );
            }
      }

      private class ScrollSateListener implements OnPageChangeListener {

            @Override
            public void onPageScrolled (
                int position, float positionOffset, int positionOffsetPixels ) {

                  mScrollPosition = position;
                  mPositionOffsetPixels = positionOffsetPixels;
                  int currentItem = getCurrentItem();
            }

            @Override
            public void onPageSelected ( int position ) {

            }

            @Override
            public void onPageScrollStateChanged ( int state ) {

                  mSate = state;
            }
      }
}
