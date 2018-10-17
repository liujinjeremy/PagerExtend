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

      private int                           mCurrentPosition;
      private int                           mState = RecyclerView.SCROLL_STATE_IDLE;
      private int                           mNextPosition;
      private int                           mOrientation;
      private int                           mDx;
      private int                           mDy;
      private OnRecyclerPagerScrollListener mOnRecyclerPagerScrollListener;

      public void setOnRecyclerPagerScrollListener (
          OnRecyclerPagerScrollListener onRecyclerPagerScrollListener ) {

            mOnRecyclerPagerScrollListener = onRecyclerPagerScrollListener;
      }

      public OnRecyclerPagerScrollListener getOnRecyclerPagerScrollListener ( ) {

            return mOnRecyclerPagerScrollListener;
      }

      public int getCurrentPosition ( ) {

            return mCurrentPosition;
      }

      public int getState ( ) {

            return mState;
      }

      @Override
      public void onScrollStateChanged ( RecyclerView recyclerView, int newState ) {

            super.onScrollStateChanged( recyclerView, newState );

            if( newState == SCROLL_STATE_IDLE ) {

                  if( mState == SCROLL_STATE_SETTLING ) {
                        mCurrentPosition = ( (LinearLayoutManager) recyclerView
                            .getLayoutManager() )
                            .findFirstVisibleItemPosition();
                  }

                  mDx = mDy = 0;
                  mOrientation = ( (LinearLayoutManager) recyclerView
                      .getLayoutManager() ).getOrientation();
            } else if( newState == SCROLL_STATE_DRAGGING ) {

                  mDx = mDy = 0;
                  mOrientation = ( (LinearLayoutManager) recyclerView
                      .getLayoutManager() ).getOrientation();
            } else if( newState == SCROLL_STATE_SETTLING ) {

                  if( mCurrentPosition != mNextPosition ) {
                        onPageSelected( mCurrentPosition, mNextPosition );
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

                  onScroll( mState, mCurrentPosition, mNextPosition, mDx, mDy );
            }
      }

      protected void onPageSelected ( int currentPosition, int nextPosition ) {

            if( mOnRecyclerPagerScrollListener != null ) {
                  mOnRecyclerPagerScrollListener.onPageSelected( currentPosition, nextPosition );
            }
      }

      protected void onScroll ( int state, int currentPosition, int nextPosition, int dx, int dy ) {

            if( mOnRecyclerPagerScrollListener != null ) {
                  mOnRecyclerPagerScrollListener
                      .onScroll( state, currentPosition, nextPosition, dx, dy );
            }
      }
}
