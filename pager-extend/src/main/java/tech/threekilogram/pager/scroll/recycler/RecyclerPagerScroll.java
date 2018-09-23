package tech.threekilogram.pager.scroll.recycler;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

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

      public static String scrollStateString ( int newState ) {

            String state = null;
            if( newState == SCROLL_STATE_IDLE ) {
                  state = "SCROLL_STATE_IDLE";
            } else if( newState == SCROLL_STATE_DRAGGING ) {
                  state = "SCROLL_STATE_DRAGGING";
            } else if( newState == SCROLL_STATE_SETTLING ) {
                  state = "SCROLL_STATE_SETTLING";
            }

            return state;
      }
}
