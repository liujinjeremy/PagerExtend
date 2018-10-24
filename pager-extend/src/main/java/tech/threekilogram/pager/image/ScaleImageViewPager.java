package tech.threekilogram.pager.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import tech.threekilogram.pager.adapter.BaseTypePagerAdapter;

/**
 * @author Liujin 2018-10-23:15:19
 */
public class ScaleImageViewPager extends ViewPager {

      /**
       * 当前滑动状态
       */
      private int         mState           = ViewPager.SCROLL_STATE_IDLE;
      /**
       * 记录是否触发缩放事件,如果触发缩放事件之后,那么滑动时间不在触发,直到下一次手指按下
       */
      private boolean     isScaleHandled;
      /**
       * 记录上一次的传递给viewpager的event
       */
      private MotionEvent mLastMotionEvent;
      /**
       * 记录移动距离
       */
      private float       mDownX;
      private float       mDownY;
      private float       mLastX;
      private float       mLastY;
      private float       mDx;
      private float       mDy;
      /**
       * 记录上一次手指抬起时间,用于双击判断
       */
      private long        mLastUpEventTime = 0;
      /**
       * 手指按下时处于idle状态时的scrollX,用于判断pager是否已经滑动,以及滑动距离
       */
      private int         mDownIdleScrollX;

      private Scroller         mScroller;
      private VelocityTracker  mTracker;
      private ScaleItemFlinger mFlinger;

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
            mScroller = new Scroller( getContext() );
            mFlinger = new ScaleItemFlinger();
      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent ev ) {

//            if( mState == ViewPager.SCROLL_STATE_SETTLING ) {
//                  return false;
//            }

            if( ev.getAction() == MotionEvent.ACTION_DOWN ) {
                  mScroller.forceFinished( true );
            }

            if( ev.getAction() == MotionEvent.ACTION_DOWN
                && mState == ViewPager.SCROLL_STATE_IDLE ) {

                  mDownIdleScrollX = getScrollX();
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

                              mLastMotionEvent = MotionEvent.obtain( ev );

                              if( mTracker == null ) {
                                    mTracker = VelocityTracker.obtain();
                              }
                              mTracker.addMovement( ev );

                              break;
                        case MotionEvent.ACTION_MOVE:

                              if( isScaleHandled ) {
                                    MotionEvent event = createMoveMotionEvent(
                                        mLastMotionEvent, 0, 0, ev );
                                    mLastMotionEvent.recycle();
                                    mLastMotionEvent = MotionEvent.obtain( event );

                                    if( mTracker != null ) {
                                          mTracker.recycle();
                                          mTracker = null;
                                    }

                                    return super.onTouchEvent( event );
                              }

                              float x = ev.getX();
                              float y = ev.getY();
                              mDx = x - mLastX;
                              mDy = y - mLastY;
                              mLastX = x;
                              mLastY = y;
                              if( mTracker != null ) {
                                    mTracker.addMovement( ev );
                              }

                              /* 先处理pager滚动 */
                              int scrollX = getScrollX();
                              if( scrollX > mDownIdleScrollX ) {
                                    if( mDx > 0 ) {
                                          int offset = mDownIdleScrollX - scrollX;

                                          float dx = mDx;
                                          if( offset + dx > 0 ) {
                                                dx = -offset;
                                          }

                                          MotionEvent event = createMoveMotionEvent(
                                              mLastMotionEvent, dx, mDy, ev );
                                          mLastMotionEvent.recycle();
                                          mLastMotionEvent = MotionEvent.obtain( event );
                                          return super.onTouchEvent( event );
                                    }
                              }
                              if( scrollX < mDownIdleScrollX ) {
                                    if( mDx < 0 ) {
                                          int offset = mDownIdleScrollX - scrollX;

                                          float dx = mDx;
                                          if( mDx + offset < 0 ) {
                                                dx = -offset;
                                          }

                                          MotionEvent event = createMoveMotionEvent(
                                              mLastMotionEvent, dx, mDy, ev );
                                          mLastMotionEvent.recycle();
                                          mLastMotionEvent = MotionEvent.obtain( event );
                                          return super.onTouchEvent( event );
                                    }
                              }

                              /* 处理 scale item 消耗距离 */
                              float movedX = scaleItemMovedX( view );
                              float movedY = scaleItemMovedY( view );
                              float dx = mDx - movedX;
                              float dy = mDy - movedY;
                              MotionEvent event = createMoveMotionEvent(
                                  mLastMotionEvent, dx, dy, ev );
                              mLastMotionEvent.recycle();
                              mLastMotionEvent = MotionEvent.obtain( event );
                              return super.onTouchEvent( event );

                        case MotionEvent.ACTION_UP:

                              if( isDoubleTap() ) {
                                    view.reset();
                              }

                              mDownX = mDownY = mLastX = mLastY = mDx = mDy = 0;

                              if( isScaleHandled ) {

                                    isScaleHandled = false;

                                    if( mTracker != null ) {
                                          mTracker.recycle();
                                          mTracker = null;
                                    }

                                    MotionEvent upMotionEvent = createUpMotionEvent(
                                        mLastMotionEvent, ev );
                                    mLastMotionEvent.recycle();
                                    mLastMotionEvent = null;
                                    return super.onTouchEvent( upMotionEvent );
                              } else {

                                    if( mTracker != null ) {

                                          mTracker.computeCurrentVelocity( 256 );
                                          float xVelocity = mTracker.getXVelocity();
                                          float yVelocity = mTracker.getYVelocity();
                                          mTracker.recycle();
                                          mTracker = null;
                                          mFlinger.startFling( view, xVelocity, yVelocity );
                                    }

                                    MotionEvent upMotionEvent = createUpMotionEvent(
                                        mLastMotionEvent, ev );
                                    mLastMotionEvent.recycle();
                                    mLastMotionEvent = null;
                                    return super.onTouchEvent( upMotionEvent );
                              }

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

      private float scaleItemMovedY ( ScaleImageView imageView ) {

            RectF rect = imageView.getDrawableRect();
            if( rect.top < 0 && mDy > 0 ) {

                  float dy = mDy;
                  if( rect.top + mDy > 0 ) {
                        dy = -rect.top;
                  }
                  imageView.setTranslateY( imageView.getTranslateY() + dy );
                  return dy;
            }

            int height = imageView.getHeight();

            if( rect.bottom > height && mDy < 0 ) {

                  float dy = mDy;
                  if( rect.bottom + mDy < height ) {
                        dy = height - rect.bottom;
                  }
                  imageView.setTranslateY( imageView.getTranslateY() + dy );
                  return dy;
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
            final int i = 256;
            if( currentTime - mLastUpEventTime < i ) {
                  return true;
            }
            mLastUpEventTime = currentTime;
            return false;
      }

      @Override
      public void computeScroll ( ) {

            super.computeScroll();

            if( mFlinger.computeScrollOffset() ) {
                  mFlinger.setScaleTranslate();
                  invalidate();
            }
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

            @Override
            public void destroyItem (
                @NonNull ViewGroup container, int position, @NonNull Object object ) {

                  if( getViewType( position ) == TYPE_SCALE_IMAGE ) {
                        getScaleImageViewAt( position ).reset();
                  }
                  super.destroyItem( container, position, object );
            }
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

      /**
       * 监听滚动状态
       */
      private class ScrollSateListener implements OnPageChangeListener {

            @Override
            public void onPageScrolled (
                int position, float positionOffset, int positionOffsetPixels ) {

            }

            @Override
            public void onPageSelected ( int position ) {

            }

            @Override
            public void onPageScrollStateChanged ( int state ) {

                  mState = state;
            }
      }

      /**
       * scale item fling
       */
      private class ScaleItemFlinger {

            private int            mXVelocity;
            private int            mYVelocity;
            private ScaleImageView mScaleImageView;
            private float          mTranslateX;
            private float          mTranslateY;

            private void startFling ( ScaleImageView imageView, float xVelocity, float yVelocity ) {

                  this.mXVelocity = (int) xVelocity;
                  this.mYVelocity = (int) yVelocity;
                  mScaleImageView = imageView;
                  RectF rect = mScaleImageView.getDrawableRect();
                  int width = mScaleImageView.getWidth();
                  int height = mScaleImageView.getHeight();
                  mTranslateX = mScaleImageView.getTranslateX();
                  mTranslateY = mScaleImageView.getTranslateY();

                  int minX = 0;
                  if( xVelocity < 0 ) {
                        if( rect.right > width ) {
                              minX = (int) ( width - rect.right );
                        }
                  }
                  int maxX = 0;
                  if( xVelocity > 0 ) {
                        if( rect.left < 0 ) {
                              maxX = (int) -rect.left;
                        }
                  }
                  int minY = 0;
                  if( yVelocity < 0 ) {
                        if( rect.bottom > height ) {
                              minY = (int) ( height - rect.bottom );
                        }
                  }
                  int maxY = 0;
                  if( yVelocity > 0 ) {
                        if( rect.top < 0 ) {
                              maxY = (int) -rect.top;
                        }
                  }

                  mScroller.fling(
                      0, 0,
                      mXVelocity, mYVelocity,
                      minX / 2, maxX / 2,
                      minY / 2, maxY / 2
                  );
                  invalidate();
            }

            private boolean computeScrollOffset ( ) {

                  return mScroller.computeScrollOffset();
            }

            private int getCurrX ( ) {

                  return mScroller.getCurrX();
            }

            private int getCurrY ( ) {

                  return mScroller.getCurrY();
            }

            private void setScaleTranslate ( ) {

                  int currX = mScroller.getCurrX();
                  int currY = mScroller.getCurrY();

                  mScaleImageView.setTranslate(
                      mTranslateX + currX * 2,
                      mTranslateY + currY * 2
                  );
            }
      }
}
