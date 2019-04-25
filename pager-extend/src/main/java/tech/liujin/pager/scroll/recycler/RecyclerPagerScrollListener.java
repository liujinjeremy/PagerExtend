package tech.liujin.pager.scroll.recycler;

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

      /**
       * 上一次状态
       */
      protected int mState = RecyclerView.SCROLL_STATE_IDLE;
      /**
       * 当前操作的item位置
       */
      protected int mCurrentPosition;
      /**
       * 滚动时下一个item位置
       */
      protected int mNextPosition;
      /**
       * 滚动方向
       */
      protected int mOrientation;
      /**
       * 滚动时移动距离
       */
      protected int mDx;
      /**
       * 滚动时移动距离
       */
      protected int mDy;

      /**
       * 获取当前操作位置
       *
       * @return 当前操作位置
       */
      public int getCurrentPosition ( ) {

            return mCurrentPosition;
      }

      /**
       * 当前状态
       */
      public int getState ( ) {

            return mState;
      }

      @Override
      public void onScrollStateChanged ( RecyclerView recyclerView, int newState ) {

            super.onScrollStateChanged( recyclerView, newState );

            if( mState == SCROLL_STATE_SETTLING && newState == SCROLL_STATE_DRAGGING ) {
                  return;
            }

            if( newState == SCROLL_STATE_IDLE ) {

                  mCurrentPosition = ( (LinearLayoutManager) recyclerView
                      .getLayoutManager() )
                      .findFirstVisibleItemPosition();

                  mDx = mDy = 0;
                  mOrientation = ( (LinearLayoutManager) recyclerView
                      .getLayoutManager() ).getOrientation();
            } else if( newState == SCROLL_STATE_DRAGGING ) {

                  mCurrentPosition = ( (LinearLayoutManager) recyclerView
                      .getLayoutManager() )
                      .findFirstVisibleItemPosition();

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

      protected void onPageSelected ( int currentPosition, int nextPosition ) { }

      protected void onScroll (
          int state, int currentPosition, int nextPosition, int dx, int dy ) { }
}
