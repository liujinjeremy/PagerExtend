package tech.threekilogram.pager.scroll.pager;

import android.support.v4.view.ViewPager;

/**
 * Created by LiuJin on 2017-12-31:8:45
 * viewPager滚动时的观察者,滚动时会通知 OnRecyclerPagerScrollListener 当前滚动的position,下个将要显示的position,以及进度值
 *
 * @author liujin
 */
class ViewPagerScrollListener implements ViewPager.OnPageChangeListener {

      /**
       * 用于获取当前pager状态
       */
      private ViewPager                 mPager;
      /**
       * 当前滚动状态
       */
      private int                       mState;
      /**
       * 当前条目索引
       */
      private int                       mCurrentIndex;
      /**
       * 监听
       */
      private OnViewPagerScrollListener mOnViewPagerScrollListener;

      /**
       * 创建
       *
       * @param pager pager
       */
      ViewPagerScrollListener ( ViewPager pager ) {

            mPager = pager;
      }

      void setOnViewPagerScrollListener (
          OnViewPagerScrollListener onViewPagerScrollListener ) {

            mOnViewPagerScrollListener = onViewPagerScrollListener;
      }

      OnViewPagerScrollListener getOnViewPagerScrollListener ( ) {

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

                  mOnViewPagerScrollListener
                      .onScroll( mState, mCurrentIndex, nextIndex, offset );
            } else {

                  float offset = -positionOffset;
                  nextIndex = position + 1;

                  if( positionOffset == 0 ) {
                        offset = -1;
                        nextIndex -= 1;
                  }

                  if( mCurrentIndex != nextIndex ) {

                        mOnViewPagerScrollListener
                            .onScroll( mState, mCurrentIndex, nextIndex, offset );
                  }
            }
      }

      @Override
      public void onPageSelected ( int position ) {

            mOnViewPagerScrollListener.onPageSelected( mCurrentIndex, position );
      }

      @Override
      public void onPageScrollStateChanged ( int state ) {

            if( state == ViewPager.SCROLL_STATE_DRAGGING ) {
                  mCurrentIndex = mPager.getCurrentItem();
            }

            mState = state;
      }
}
