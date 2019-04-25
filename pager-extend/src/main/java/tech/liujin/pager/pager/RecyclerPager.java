package tech.liujin.pager.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Liujin 2018-09-19:16:05
 */
public class RecyclerPager extends RecyclerView {

      /**
       * 记录当前操作的item
       */
      private int mCurrentPosition;

      public RecyclerPager ( Context context ) {

            this( context, null, 0 );
      }

      public RecyclerPager (
          Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public RecyclerPager ( Context context, @Nullable AttributeSet attrs, int defStyle ) {

            super( context, attrs, defStyle );
            init();
      }

      private void init ( ) {

            PagerLinearLayoutManager layoutManager =
                new PagerLinearLayoutManager( getContext() );
            layoutManager.setOrientation( HORIZONTAL );
            setLayoutManager( layoutManager );

            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView( this );

            addOnScrollListener( new GetCurrentScrollListener() );
      }

      public void setOrientation ( @RecyclerPager.Orientation int orientation ) {

            ( (LinearLayoutManager) getLayoutManager() ).setOrientation( orientation );
      }

      public int getCurrentPosition ( ) {

            return mCurrentPosition;
      }

      public View getCurrentView ( ) {

            return getLayoutManager().findViewByPosition( mCurrentPosition );
      }

      @Override
      public void scrollToPosition ( int position ) {

            super.scrollToPosition( position );
            mCurrentPosition = position;
      }

      @Override
      public void smoothScrollToPosition ( int position ) {

            super.smoothScrollToPosition( position );
            mCurrentPosition = position;
      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent ev ) {

            /* 按下时获取当前item */
            if( ev.getAction() == MotionEvent.ACTION_DOWN
                || ev.getAction() == MotionEvent.ACTION_UP ) {

                  if( getScrollState() == RecyclerView.SCROLL_STATE_IDLE ) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                        mCurrentPosition = layoutManager.findFirstVisibleItemPosition();
                  }
            }

            return super.dispatchTouchEvent( ev );
      }

      /**
       * 处理滚动事件,滚动时更新当前状态
       */
      private class GetCurrentScrollListener extends OnScrollListener {

            @Override
            public void onScrollStateChanged ( RecyclerView recyclerView, int newState ) {

                  if( newState == RecyclerView.SCROLL_STATE_IDLE ) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                        mCurrentPosition = layoutManager
                            .findFirstVisibleItemPosition();
                  }
            }

            @Override
            public void onScrolled ( RecyclerView recyclerView, int dx, int dy ) {

                  if( dx == 0 && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE ) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                        mCurrentPosition = layoutManager
                            .findFirstVisibleItemPosition();
                  }
            }
      }
}
