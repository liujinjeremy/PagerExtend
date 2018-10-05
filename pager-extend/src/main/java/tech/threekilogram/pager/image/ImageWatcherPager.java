package tech.threekilogram.pager.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.Locale;
import tech.threekilogram.pager.R;
import tech.threekilogram.pager.pager.RecyclerPager;
import tech.threekilogram.pager.scroll.recycler.OnRecyclerPagerScrollListener;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScroll;

/**
 * @author Liujin 2018-10-05:11:18
 */
public class ImageWatcherPager extends FrameLayout {

      private static final String TAG = ImageWatcherPager.class.getSimpleName();

      /**
       * pager
       */
      private RecyclerPager       mRecyclerPager;
      /**
       * pager adapter
       */
      private ImageWatcherAdapter mImageWatcherAdapter;
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

            mItemTouchListener = new ItemTouchListener();
            mRecyclerPager.addOnItemTouchListener( mItemTouchListener );

            mRecyclerPagerScroll = new RecyclerPagerScroll( mRecyclerPager );
            mRecyclerPagerScroll.setOnRecyclerPagerScrollListener( new PagerScrollListener() );

            mRecyclerPager.setAdapter( new PagerAdapter() );
      }

      public void setImageWatcherAdapter (
          ImageWatcherAdapter imageWatcherAdapter ) {

            mImageWatcherAdapter = imageWatcherAdapter;
            Adapter adapter = mRecyclerPager.getAdapter();
            if( adapter != null ) {
                  adapter.notifyDataSetChanged();
            }
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

      private class PagerAdapter extends RecyclerView.Adapter<ImageViewHolder> {

            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  ScaleImageView view = (ScaleImageView) LayoutInflater.from( parent.getContext() )
                                                                       .inflate(
                                                                           R.layout.item_image,
                                                                           parent, false
                                                                       );
                  view.setMinCanvasScaleX( 1f );
                  view.setMinCanvasScaleY( 1f );
                  return new ImageViewHolder( view );
            }

            @Override
            public void onBindViewHolder (
                @NonNull ImageViewHolder holder, int position ) {

                  if( mImageWatcherAdapter != null ) {
                        Bitmap image = mImageWatcherAdapter.getImage( position );
                        holder.bindImage( position, image );
                  }
            }

            @Override
            public int getItemCount ( ) {

                  return mImageWatcherAdapter == null ? 0 : mImageWatcherAdapter.getImageCount();
            }
      }

      private class ImageViewHolder extends RecyclerView.ViewHolder {

            private ImageViewHolder ( View itemView ) {

                  super( itemView );
            }

            private void bindImage ( int position, Bitmap image ) {

                  ( (ScaleImageView) itemView ).setImageBitmap( image );
            }
      }

      private class ItemTouchListener implements RecyclerView.OnItemTouchListener {

            @Override
            public boolean onInterceptTouchEvent ( RecyclerView rv, MotionEvent e ) {

                  if( mRecyclerPagerScroll.getState() != RecyclerView.SCROLL_STATE_IDLE ) {
                        return false;
                  }

                  switch( e.getAction() ) {

                        case MotionEvent.ACTION_DOWN:
                              if( mRecyclerPagerScroll.getState()
                                  == RecyclerView.SCROLL_STATE_IDLE ) {
                                    int currentItem = mRecyclerPagerScroll.getCurrentPosition();
                                    mItemView = mRecyclerPager.findItemView( currentItem );
                              }
                              break;
                        case MotionEvent.ACTION_MOVE:
                              if( mItemView != null && !isScaleTouch ) {
                                    RectF drawableRect = mItemView.getDrawableRect();

                                    if( Math.abs( mDx ) >= Math.abs( mDy ) ) {

                                          String format = String
                                              .format( Locale.CANADA, "%.4f", mDx );
                                          Log.e(
                                              TAG, "onInterceptTouchEvent : " + format + " "
                                                  + drawableRect );

                                          /* 水平滑动 */
                                          if( mDx > 0 && drawableRect.left < 0 ) {
                                                Log.e( TAG, "onInterceptTouchEvent : true" );
                                                return true;
                                          }
                                          if( mDx < 0 &&
                                              drawableRect.right > mItemView.getWidth() ) {
                                                Log.e( TAG, "onInterceptTouchEvent : " + true );
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

      private class PagerScrollListener implements OnRecyclerPagerScrollListener {

            @Override
            public void onScroll (
                int state, int currentPosition, int nextPosition, int offsetX, int offsetY ) { }

            @Override
            public void onPageSelected ( int prevSelected, int newSelected ) { }
      }

      /**
       * 辅助工作,获取图片
       */
      @SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
      public static abstract class ImageWatcherAdapter {

            /**
             * 数量
             *
             * @return image数量
             */
            protected abstract int getImageCount ( );

            /**
             * 获取位于该位置的图片
             *
             * @param position 位置
             *
             * @return 该位置图片
             */
            protected abstract Bitmap getImage ( int position );
      }
}
