package tech.threekilogram.pager.scroll.recycler;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

/**
 * @author Liujin 2018-09-23:9:49
 */
public class RecyclerPagerScrollListener extends OnScrollListener {

      private RecyclerView mRecyclerView;
      private int          mCurrentPosition;
      private int          mState;
      private int          mNextPosition;
      private int          mOrientation;
      private int          mDx;
      private int          mDy;
      private boolean isCallSelected = false;
      private OnRecyclerPagerScrollListener mOnRecyclerPagerScrollListener;

      public RecyclerPagerScrollListener ( RecyclerView recyclerView ) {

            mRecyclerView = recyclerView;
      }

      public void setOnRecyclerPagerScrollListener (
          OnRecyclerPagerScrollListener onRecyclerPagerScrollListener ) {

            mOnRecyclerPagerScrollListener = onRecyclerPagerScrollListener;
      }

      public OnRecyclerPagerScrollListener getOnRecyclerPagerScrollListener ( ) {

            return mOnRecyclerPagerScrollListener;
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

      @Override
      public void onScrollStateChanged ( RecyclerView recyclerView, int newState ) {

            super.onScrollStateChanged( recyclerView, newState );

            if( newState == SCROLL_STATE_IDLE ) {
                  if( mState == SCROLL_STATE_SETTLING ) {
                        mCurrentPosition = ( (LinearLayoutManager) mRecyclerView
                            .getLayoutManager() )
                            .findFirstVisibleItemPosition();
                  }
            } else if( newState == SCROLL_STATE_DRAGGING ) {
                  mDx = mDy = 0;
                  mOrientation = ( (LinearLayoutManager) mRecyclerView
                      .getLayoutManager() ).getOrientation();
                  isCallSelected = false;
            } else if( newState == SCROLL_STATE_SETTLING ) {
                  if( mState == SCROLL_STATE_DRAGGING ) {
                        mOnRecyclerPagerScrollListener
                            .onPageSelected( mCurrentPosition, mNextPosition );
                  }
            }

            mState = newState;
      }

      @Override
      public void onScrolled ( RecyclerView recyclerView, int dx, int dy ) {

            super.onScrolled( recyclerView, dx, dy );

            if( mOrientation == RecyclerView.HORIZONTAL ) {

                  mDx += dx;

                  if( mDx > 0 ) {
                        mNextPosition = mCurrentPosition + 1;
                  } else if( mDx < 0 ) {
                        mNextPosition = mCurrentPosition - 1;
                  }
            } else {

                  mDy += dy;

                  if( mDy > 0 ) {
                        mNextPosition = mCurrentPosition + 1;
                  } else if( mDy < 0 ) {
                        mNextPosition = mCurrentPosition - 1;
                  }
            }

            if( mCurrentPosition != mNextPosition ) {

                  mOnRecyclerPagerScrollListener
                      .onScroll( mState, mCurrentPosition, mNextPosition, mDx, mDy );
            }
      }
}
