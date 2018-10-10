package tech.threekilogram.pager.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import tech.threekilogram.pager.scroll.recycler.OnRecyclerPagerScrollListener;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScroll;

/**
 * @author Liujin 2018-09-19:16:05
 */
public class RecyclerPager extends RecyclerView {

      private int                 mCurrentPosition;
      private RecyclerPagerScroll mRecyclerPagerScroll;

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

            mRecyclerPagerScroll = new RecyclerPagerScroll( this );
            mRecyclerPagerScroll
                .setOnRecyclerPagerScrollListener( new OnScrollGetPositionListener() );
      }

      public void setOrientation ( @RecyclerPager.Orientation int orientation ) {

            ( (LinearLayoutManager) getLayoutManager() ).setOrientation( orientation );
      }

      public int findFirstCompletelyVisibleItemPosition ( ) {

            return ( (LinearLayoutManager) getLayoutManager() )
                .findFirstCompletelyVisibleItemPosition();
      }

      public int findFirstVisibleItemPosition ( ) {

            return ( (LinearLayoutManager) getLayoutManager() )
                .findFirstVisibleItemPosition();
      }

      public int findLastCompletelyVisibleItemPosition ( ) {

            return ( (LinearLayoutManager) getLayoutManager() )
                .findLastCompletelyVisibleItemPosition();
      }

      public int findLastVisibleItemPosition ( ) {

            return ( (LinearLayoutManager) getLayoutManager() )
                .findLastVisibleItemPosition();
      }

      @SuppressWarnings("unchecked")
      public <T extends View> T findItemView ( int position ) {

            ViewHolder holder = findViewHolderForLayoutPosition( position );

            return holder == null ? null : (T) holder.itemView;
      }

      @Override
      public void scrollToPosition ( int position ) {

            mCurrentPosition = position;
            super.scrollToPosition( position );
      }

      @Override
      public void smoothScrollToPosition ( int position ) {

            mCurrentPosition = position;
            super.smoothScrollToPosition( position );
      }

      public int getCurrentPosition ( ) {

            return mCurrentPosition;
      }

      @Nullable
      public <T extends View> T getCurrentItem ( ) {

            return findItemView( mCurrentPosition );
      }

      private class OnScrollGetPositionListener implements OnRecyclerPagerScrollListener {

            @Override
            public void onScroll (
                int state, int currentPosition, int nextPosition, int offsetX, int offsetY ) {

            }

            @Override
            public void onPageSelected ( int prevSelected, int newSelected ) {

                  mCurrentPosition = newSelected;
            }
      }
}
