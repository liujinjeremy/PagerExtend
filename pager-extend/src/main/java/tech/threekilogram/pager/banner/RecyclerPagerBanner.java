package tech.threekilogram.pager.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import tech.threekilogram.pager.pager.DurationAddSmoothScroller;
import tech.threekilogram.pager.pager.RecyclerPager;

/**
 * @author Liujin 2018-09-22:11:17
 */
public class RecyclerPagerBanner extends LoopHandlerLayout {

      protected RecyclerPager             mRecyclerPager;
      protected DurationAddSmoothScroller mSmoothScroller;

      public RecyclerPagerBanner ( @NonNull Context context ) {

            this( context, null, 0 );
      }

      public RecyclerPagerBanner (
          @NonNull Context context,
          @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public RecyclerPagerBanner (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
      }

      @Override
      protected void init ( ) {

            super.init();
            mSmoothScroller = new DurationAddSmoothScroller( getContext() );
            mSmoothScroller.setDurationAdded( 120 );

            mRecyclerPager = createPager( getContext() );
            addView( mRecyclerPager, 0 );
      }

      protected RecyclerPager createPager ( Context context ) {

            return new RecyclerPager( context );
      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent ev ) {

            onTouchPauseLoop( ev );
            return super.dispatchTouchEvent( ev );
      }

      public RecyclerView getRecyclerPager ( ) {

            return mRecyclerPager;
      }

      /**
       * 为{@link #mRecyclerPager}设置Adapter
       */
      public void setBannerAdapter ( BannerAdapter adapter ) {

            mRecyclerPager.setAdapter( adapter );
            mRecyclerPager.scrollToPosition( adapter.getStartPosition() );

            if( mOnLoopListener == null || !( mOnLoopListener instanceof BannerOnLoopListener ) ) {

                  mOnLoopListener = new BannerOnLoopListener();
            }
            setOnLoopListener( mOnLoopListener );
      }

      /**
       * 获取{@link #mRecyclerPager}设置的Adapter
       */
      public BannerAdapter getBannerAdapter ( ) {

            return (BannerAdapter) mRecyclerPager.getAdapter();
      }

      public void addScrollDuration ( int duration ) {

            mSmoothScroller.setDurationAdded( duration );
      }

      public int getAddScrollDuration ( ) {

            return mSmoothScroller.getDurationAdded();
      }

      public void addOnScrollListener ( OnScrollListener listener ) {

            mRecyclerPager.addOnScrollListener( listener );
      }

      public void removeOnScrollListener ( OnScrollListener listener ) {

            mRecyclerPager.removeOnScrollListener( listener );
      }

      public void clearOnScrollListeners ( ) {

            mRecyclerPager.clearOnScrollListeners();
      }

      public void smoothToNext ( ) {

            LayoutManager layoutManager = mRecyclerPager
                .getLayoutManager();
            int itemPosition = ( (LinearLayoutManager) layoutManager )
                .findFirstCompletelyVisibleItemPosition();

            if( itemPosition + 1 < Integer.MAX_VALUE ) {
                  mSmoothScroller.setTargetPosition( itemPosition + 1 );
                  layoutManager.startSmoothScroll( mSmoothScroller );
            }
      }

      public void smoothToPrev ( ) {

            int itemPosition = ( (LinearLayoutManager) mRecyclerPager
                .getLayoutManager() )
                .findFirstCompletelyVisibleItemPosition();

            if( itemPosition - 1 >= 0 ) {
                  mRecyclerPager.smoothScrollToPosition( itemPosition - 1 );
            }
      }

      /**
       * loop action
       */
      private class BannerOnLoopListener implements OnLoopListener {

            @Override
            public void onLoop ( LoopHandlerLayout layout ) {

                  smoothToNext();
            }
      }

      /**
       * base adapter
       */
      public static abstract class BannerAdapter<VH extends ViewHolder> extends
                                                                        RecyclerView.Adapter<VH> {

            @Override
            public int getItemCount ( ) {

                  return Integer.MAX_VALUE;
            }

            /**
             * 实际数据数量
             *
             * @return 数据数量
             */
            public abstract int getActualCount ( );

            public int getStartPosition ( ) {

                  int i = Integer.MAX_VALUE / 2;
                  return i - ( i % getActualCount() );
            }

            /**
             * 将loopViewPager的position 转换成 mAdapter的Position
             *
             * @param position MaxCountAdapter 中的位置
             *
             * @return mPagerAdapter 中的位置
             */
            protected int getActualPosition ( int position ) {

                  return position % getActualCount();
            }
      }
}
