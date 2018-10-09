package tech.threekilogram.pager.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import tech.threekilogram.pager.pager.RecyclerPager;
import tech.threekilogram.pager.scroll.recycler.OnRecyclerPagerScrollListener;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScroll;

/**
 * @author Liujin 2018-10-05:11:18
 */
public class ImageWatcherPager extends FrameLayout {

      /**
       * pager
       */
      private RecyclerPager       mRecyclerPager;
      /**
       * item touch
       */
      private ItemTouchListener   mItemTouchListener;
      /**
       * current item
       */
      private ScaleImageView      mItemView;
      /**
       * pager scroll
       */
      private RecyclerPagerScroll mRecyclerPagerScroll;

      /**
       * recode move distance
       */
      private float   mLastX;
      private float   mLastY;
      private float   mDx;
      private float   mDy;
      /**
       * recode is scale touched
       */
      private boolean isScaleTouch;

      public ImageWatcherPager ( @NonNull Context context ) {

            this( context, null, 0 );
      }

      public ImageWatcherPager (
          @NonNull Context context,
          @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public ImageWatcherPager (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init( context );
      }

      private void init ( Context context ) {

            mRecyclerPager = new RecyclerPager( context );
            addView( mRecyclerPager );

            /* 拦截手势事件 */
            mItemTouchListener = new ItemTouchListener();
            mRecyclerPager.addOnItemTouchListener( mItemTouchListener );

            /* 用于获取当前状态 */
            mRecyclerPagerScroll = new RecyclerPagerScroll( mRecyclerPager );
            mRecyclerPagerScroll.setOnRecyclerPagerScrollListener( new PagerScrollListener() );
      }

      public RecyclerPager getRecyclerPager ( ) {

            return mRecyclerPager;
      }

      /**
       * 设置{@link #mRecyclerPager}的adapter
       */
      public void setImageWatcherAdapter ( ImageWatcherAdapter imageWatcherAdapter ) {

            mRecyclerPager.setAdapter( imageWatcherAdapter );
      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent e ) {

            if( e.getPointerCount() > 1 ) {
                  isScaleTouch = true;
            }

            switch( e.getAction() ) {

                  case MotionEvent.ACTION_DOWN:
                        mLastX = e.getX();
                        mLastY = e.getY();
                        break;
                  case MotionEvent.ACTION_MOVE:
                        float x = e.getX();
                        float y = e.getY();

                        mDx = x - mLastX;
                        mDy = y - mLastY;

                        mLastX = x;
                        mLastY = y;
                        break;
                  case MotionEvent.ACTION_UP:
                        mLastX = mLastY = mDx = mDy = 0;
                        isScaleTouch = false;
                        break;
                  default:
                        break;
            }

            return super.dispatchTouchEvent( e );
      }

      /**
       * 主要用于构建一个item是{@link ScaleImageView}的adapter
       */
      public static abstract class ImageWatcherAdapter extends
                                                       RecyclerView.Adapter<ImageViewHolder> {

            /**
             * 获取位于该位置的图片
             *
             * @param position 位置
             *
             * @return 该位置图片
             */
            protected abstract Bitmap getImage ( int position );

            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  return new ImageViewHolder( createScaleImageView( parent.getContext() ) );
            }

            /**
             * 创建{@link ScaleImageView}
             */
            protected ScaleImageView createScaleImageView ( Context context ) {

                  ScaleImageView view = new ScaleImageView( context );
                  view.setMinCanvasScaleX( 1f );
                  view.setMinCanvasScaleY( 1f );
                  return view;
            }

            @Override
            public void onBindViewHolder (
                @NonNull ImageViewHolder holder, int position ) {

                  holder.bindImage( position, getImage( position ) );
            }
      }

      /**
       * ViewHolder
       */
      public static class ImageViewHolder extends RecyclerView.ViewHolder {

            public ImageViewHolder ( View itemView ) {

                  super( itemView );
            }

            public void bindImage ( int position, Bitmap image ) {

                  ( (ScaleImageView) itemView ).setImageBitmap( image );
            }

            public ScaleImageView getItemView ( ) {

                  return (ScaleImageView) itemView;
            }
      }

      /**
       * 处理触摸事件
       */
      private class ItemTouchListener implements RecyclerView.OnItemTouchListener {

            @Override
            public boolean onInterceptTouchEvent ( RecyclerView rv, MotionEvent e ) {

                  int state = mRecyclerPagerScroll.getState();

                  if( state == RecyclerView.SCROLL_STATE_SETTLING ) {
                        return false;
                  }
                  if( state == RecyclerView.SCROLL_STATE_DRAGGING ) {
                        if( mItemView != null && !isScaleTouch ) {
                              RectF drawableRect = mItemView.getDrawableRect();

                              if( Math.abs( mDx ) >= Math.abs( mDy ) ) {

                                    /* 水平滑动 */
                                    if( mDx > 0 && drawableRect.left < 0 ) {
                                          return true;
                                    }
                                    if( mDx < 0 &&
                                        drawableRect.right > mItemView.getWidth() ) {
                                          return true;
                                    }
                              }
                        }
                        return false;
                  }

                  switch( e.getAction() ) {

                        case MotionEvent.ACTION_DOWN:
                              if( state
                                  == RecyclerView.SCROLL_STATE_IDLE ) {
                                    int currentItem = mRecyclerPagerScroll.getCurrentPosition();
                                    mItemView = mRecyclerPager.findItemView( currentItem );
                              }
                              break;
                        case MotionEvent.ACTION_MOVE:
                              if( mItemView != null && !isScaleTouch ) {
                                    RectF drawableRect = mItemView.getDrawableRect();

                                    if( Math.abs( mDx ) >= Math.abs( mDy ) ) {

                                          /* 水平滑动 */
                                          if( mDx > 0 && drawableRect.left < 0 ) {
                                                return true;
                                          }
                                          if( mDx < 0 &&
                                              drawableRect.right > mItemView.getWidth() ) {
                                                return true;
                                          }
                                    } else {
                                          /* 竖直滑动 */
                                          return true;
                                    }
                              }
                              break;
                        default:
                              break;
                  }

                  if( e.getPointerCount() > 1 ) {
                        return true;
                  }

                  return false;
            }

            @Override
            public void onTouchEvent ( RecyclerView rv, MotionEvent e ) {

                  if( mItemView == null ) {
                        return;
                  }

                  if( e.getPointerCount() > 1 ) {
                        mItemView.handleScaleGesture( e );
                        return;
                  }

                  if( e.getAction() == MotionEvent.ACTION_MOVE && !isScaleTouch ) {

                        RectF drawableRect = mItemView.getDrawableRect();

                        if( Math.abs( mDx ) >= Math.abs( mDy ) ) {
                              /* 水平滑动 */
                              float dx = mDx;
                              if( drawableRect.left <= 0 && mDx > 0
                                  && drawableRect.left + mDx >= 0 ) {

                                    dx = -drawableRect.left;
                              } else {

                                    int width = mItemView.getWidth();
                                    float right = drawableRect.right;
                                    if( right >= width && mDx < 0
                                        && right + mDx <= width ) {

                                          dx = width - right;
                                    }
                              }

                              mItemView.setTranslateX( mItemView.getTranslateX() + dx );
                        } else {

                              /* 竖直滑动 */
                              mItemView.setTranslateY( mItemView.getTranslateY() + mDy );
                        }
                  }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent ( boolean disallowIntercept ) {

            }
      }

      /**
       * 获取状态
       */
      private class PagerScrollListener implements OnRecyclerPagerScrollListener {

            @Override
            public void onScroll (
                int state, int currentPosition, int nextPosition, int offsetX, int offsetY ) { }

            @Override
            public void onPageSelected ( int prevSelected, int newSelected ) { }
      }
}
