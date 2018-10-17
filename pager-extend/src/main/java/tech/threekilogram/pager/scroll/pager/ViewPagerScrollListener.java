package tech.threekilogram.pager.scroll.pager;

import android.support.v4.view.ViewPager;

/**
 * Created by LiuJin on 2017-12-31:8:45
 * viewPager滚动时的观察者,滚动时会通知 OnRecyclerPagerScrollListener 当前滚动的position,下个将要显示的position,以及进度值
 *
 * @author liujin
 */
public class ViewPagerScrollListener implements ViewPager.OnPageChangeListener {

      /**
       * 用于获取当前pager状态
       */
      protected ViewPager                 mPager;
      /**
       * 当前滚动状态
       */
      protected int                       mState;
      /**
       * 当前条目索引
       */
      protected int                       mCurrentIndex;
      /**
       * 监听
       */
      protected OnViewPagerScrollListener mOnViewPagerScrollListener;

      /**
       * 创建
       *
       * @param pager pager
       */
      public ViewPagerScrollListener ( ViewPager pager ) {

            mPager = pager;
      }

      public void setOnViewPagerScrollListener (
          OnViewPagerScrollListener onViewPagerScrollListener ) {

            mOnViewPagerScrollListener = onViewPagerScrollListener;
      }

      public OnViewPagerScrollListener getOnViewPagerScrollListener ( ) {

            return mOnViewPagerScrollListener;
      }

      @Override
      public void onPageScrolled ( int position, float positionOffset, int positionOffsetPixels ) {

            //滚动时下一个条目索引
            int nextIndex;
            if( position < mCurrentIndex ) {

                  float offset = 1 - positionOffset;
                  nextIndex = position;
                  if( positionOffset == 0 ) {
                        offset = 1;
                  }

                  onScroll( mState, mCurrentIndex, nextIndex, offset );
            } else {

                  float offset = -positionOffset;
                  nextIndex = position + 1;

                  if( positionOffset == 0 ) {
                        offset = -1;
                        nextIndex -= 1;
                  }

                  if( mCurrentIndex != nextIndex ) {

                        onScroll( mState, mCurrentIndex, nextIndex, offset );
                  }
            }
      }

      protected void onScroll ( int state, int currentIndex, int nextIndex, float offset ) {

            if( mOnViewPagerScrollListener != null ) {
                  mOnViewPagerScrollListener.onScroll( state, currentIndex, nextIndex, offset );
            }
      }

      @Override
      public void onPageSelected ( int position ) {

            onPageSelected( mCurrentIndex, position );
      }

      protected void onPageSelected ( int currentIndex, int position ) {

            if( mOnViewPagerScrollListener != null ) {
                  mOnViewPagerScrollListener.onPageSelected( currentIndex, position );
            }
      }

      @Override
      public void onPageScrollStateChanged ( int state ) {

            if( state == ViewPager.SCROLL_STATE_DRAGGING ) {
                  mCurrentIndex = mPager.getCurrentItem();
            }

            mState = state;
      }
}
