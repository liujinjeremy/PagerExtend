package tech.threekilogram.pager.scroll.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * @author Liujin 2018-09-23:11:27
 */
public class RecyclerPagerScroll {

      private RecyclerView                mRecyclerView;
      private RecyclerPagerScrollListener mRecyclerPagerScrollListener;

      public RecyclerPagerScroll ( RecyclerView recyclerView ) {

            mRecyclerView = recyclerView;
      }

      public void setOnRecyclerPagerScrollListener (
          OnRecyclerPagerScrollListener onRecyclerPagerScrollListener ) {

            if( onRecyclerPagerScrollListener == null ) {
                  return;
            }

            if( mRecyclerPagerScrollListener == null ) {
                  mRecyclerPagerScrollListener = new RecyclerPagerScrollListener( mRecyclerView );
            }
            mRecyclerPagerScrollListener
                .setOnRecyclerPagerScrollListener( onRecyclerPagerScrollListener );

            mRecyclerView.removeOnScrollListener( mRecyclerPagerScrollListener );
            mRecyclerView.addOnScrollListener( mRecyclerPagerScrollListener );
      }

      public OnRecyclerPagerScrollListener getOnRecyclerPagerScrollListener ( ) {

            return mRecyclerPagerScrollListener == null ? null
                : mRecyclerPagerScrollListener.getOnRecyclerPagerScrollListener();
      }
}
