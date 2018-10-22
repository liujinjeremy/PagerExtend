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

                  onScroll( mState, mCurrentIndex, nextIndex, offset, positionOffsetPixels );
            } else {

                  float offset = -positionOffset;
                  nextIndex = position + 1;

                  if( positionOffset == 0 ) {
                        offset = -1;
                        nextIndex -= 1;
                  }

                  if( mCurrentIndex != nextIndex ) {

                        onScroll( mState, mCurrentIndex, nextIndex, offset, positionOffsetPixels );
                  }
            }
      }

      protected void onScroll (
          int state, int currentIndex, int nextIndex, float offset, int offsetPix ) {

            if( mOnViewPagerScrollListener != null ) {
                  mOnViewPagerScrollListener
                      .onScroll( state, currentIndex, nextIndex, offset, offsetPix );
            }
      }

      @Override
      public void onPageSelected ( int position ) {

            onPageSelected( mCurrentIndex, position );
      }

      /**
       * 当页面选中时回调
       *
       * @param prev 前一个选中的
       * @param current 当前选中的
       */
      protected void onPageSelected ( int prev, int current ) {

            if( mOnViewPagerScrollListener != null ) {
                  mOnViewPagerScrollListener.onPageSelected( prev, current );
            }
      }

      @Override
      public void onPageScrollStateChanged ( int state ) {

            if( state == ViewPager.SCROLL_STATE_DRAGGING || state == ViewPager.SCROLL_STATE_IDLE ) {
                  mCurrentIndex = mPager.getCurrentItem();
            }

            mState = state;
      }
}
