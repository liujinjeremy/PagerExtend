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
      private int                       mCurrentState;
      /**
       * 当前条目索引
       */
      private int                       mStartIndex;
      /**
       * 滚动时下一个条目索引
       */
      private int                       mNextIndex;
      /**
       * 是否象征方向滚动
       */
      private boolean                   isLeft;
      /**
       * 滚动时回调
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

      /**
       * 设置监听
       */
      void setOnViewPagerScrollListener ( OnViewPagerScrollListener onViewPagerScrollListener ) {

            mOnViewPagerScrollListener = onViewPagerScrollListener;
      }

      /**
       * 获取设置的监听
       */
      OnViewPagerScrollListener getOnViewPagerScrollListener ( ) {

            return mOnViewPagerScrollListener;
      }

      @Override
      public void onPageScrolled ( int position, float positionOffset, int positionOffsetPixels ) {

            if( mOnViewPagerScrollListener == null ) {
                  return;
            }

            if( mCurrentState == ViewPager.SCROLL_STATE_SETTLING ) {

                  final float endFlag = 0.f;

                  if( positionOffset == endFlag ) {
                        int itemPosition = mPager.getCurrentItem();

                        if( isLeft ) {
                              if( mStartIndex != itemPosition ) {
                                    mOnViewPagerScrollListener.onCurrent( mStartIndex, -1.f );
                                    mOnViewPagerScrollListener.onNext( mNextIndex, 0.f );
                              } else {
                                    mOnViewPagerScrollListener.onCurrent( mStartIndex, 0.f );
                                    mOnViewPagerScrollListener.onNext( mNextIndex, 1.f );
                              }
                        } else {
                              if( mStartIndex != itemPosition ) {
                                    mOnViewPagerScrollListener.onCurrent( mStartIndex, 1.f );
                                    mOnViewPagerScrollListener.onNext( mNextIndex, 0.f );
                              } else {
                                    mOnViewPagerScrollListener.onCurrent( mStartIndex, 0.f );
                                    mOnViewPagerScrollListener.onNext( mNextIndex, -1.f );
                              }
                        }
                        return;
                  }

                  if( mStartIndex == position ) {
                        isLeft = true;
                        mNextIndex = mStartIndex + 1;
                        if( mNextIndex == mPager.getAdapter().getCount() ) {
                              mNextIndex = 0;
                        }
                        mOnViewPagerScrollListener.onCurrent( mStartIndex, -positionOffset );
                        mOnViewPagerScrollListener.onNext( mNextIndex, 1 - positionOffset );
                  } else {
                        isLeft = false;
                        mNextIndex = position;
                        if( mStartIndex == 0 ) {
                              mNextIndex = mPager.getAdapter().getCount() - 1;
                        }
                        mOnViewPagerScrollListener.onCurrent( mStartIndex, 1 - positionOffset );
                        mOnViewPagerScrollListener.onNext( mNextIndex, -positionOffset );
                  }
            }

            if( mCurrentState == ViewPager.SCROLL_STATE_DRAGGING ) {

                  final float endFlag = 0.f;

                  if( positionOffset == endFlag ) {
                        return;
                  }

                  if( mStartIndex == position ) {
                        isLeft = true;
                        mNextIndex = mStartIndex + 1;
                        if( mNextIndex == mPager.getAdapter().getCount() ) {
                              mNextIndex = 0;
                        }
                        mOnViewPagerScrollListener.onCurrent( mStartIndex, -positionOffset );
                        mOnViewPagerScrollListener.onNext( mNextIndex, 1 - positionOffset );
                  } else {
                        isLeft = false;
                        mNextIndex = position;
                        if( mStartIndex == 0 ) {
                              mNextIndex = mPager.getAdapter().getCount() - 1;
                        }
                        mOnViewPagerScrollListener.onCurrent( mStartIndex, 1 - positionOffset );
                        mOnViewPagerScrollListener.onNext( mNextIndex, -positionOffset );
                  }
            }
      }

      @Override
      public void onPageSelected ( int position ) {

            if( mOnViewPagerScrollListener != null ) {
                  mOnViewPagerScrollListener.onPageSelected( mStartIndex, position );
            }
      }

      @Override
      public void onPageScrollStateChanged ( int state ) {

            mCurrentState = state;
            if( state == ViewPager.SCROLL_STATE_DRAGGING || state == ViewPager.SCROLL_STATE_IDLE ) {
                  mStartIndex = mPager.getCurrentItem();
            }
      }
}
