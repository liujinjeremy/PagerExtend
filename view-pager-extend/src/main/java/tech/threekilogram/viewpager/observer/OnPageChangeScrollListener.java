package tech.threekilogram.viewpager.observer;

import android.support.v4.view.ViewPager;

/**
 * Created by LiuJin on 2017-12-31:8:45
 * viewPager滚动时的观察者,滚动时会通知 OnPagerScrollObserver 当前滚动的position,下个将要显示的position,以及进度值
 *
 * @author liujin
 */
class OnPageChangeScrollListener implements ViewPager.OnPageChangeListener {

      /**
       * 用于获取当前pager状态
       */
      private ItemViewGroup         mPager;
      /**
       * 滚动时回调
       */
      private OnPagerScrollObserver mObserver;

      /**
       * 当前滚动状态
       */
      private int     mCurrentState;
      /**
       * 当前条目索引
       */
      private int     mStartIndex;
      /**
       * 滚动时下一个条目索引
       */
      private int     mNextIndex;
      /**
       * 是否象征方向滚动
       */
      private boolean isLeft;

      OnPageChangeScrollListener ( ItemViewGroup pager, OnPagerScrollObserver observer ) {

            mPager = pager;
            mObserver = observer;
      }

      @Override
      public void onPageScrolled ( int position, float positionOffset, int positionOffsetPixels ) {

            if( mObserver == null ) {
                  return;
            }

            if( mCurrentState == ViewPager.SCROLL_STATE_SETTLING ) {

                  final float endFlag = 0.f;

                  if( positionOffset == endFlag ) {
                        int itemPosition = mPager.getCurrentItemPosition();

                        if( isLeft ) {
                              if( mStartIndex != itemPosition ) {
                                    mObserver.onCurrent( mStartIndex, -1.f );
                                    mObserver.onNext( mNextIndex, 0.f );
                              } else {
                                    mObserver.onCurrent( mStartIndex, 0.f );
                                    mObserver.onNext( mNextIndex, 1.f );
                              }
                        } else {
                              if( mStartIndex != itemPosition ) {
                                    mObserver.onCurrent( mStartIndex, 1.f );
                                    mObserver.onNext( mNextIndex, 0.f );
                              } else {
                                    mObserver.onCurrent( mStartIndex, 0.f );
                                    mObserver.onNext( mNextIndex, -1.f );
                              }
                        }
                        return;
                  }

                  if( mStartIndex == position ) {
                        isLeft = true;
                        mNextIndex = mStartIndex + 1;
                        if( mNextIndex == mPager.getItemCount() ) {
                              mNextIndex = 0;
                        }
                        mObserver.onCurrent( mStartIndex, -positionOffset );
                        mObserver.onNext( mNextIndex, 1 - positionOffset );
                  } else {
                        isLeft = false;
                        mNextIndex = position;
                        if( mStartIndex == 0 ) {
                              mNextIndex = mPager.getItemCount() - 1;
                        }
                        mObserver.onCurrent( mStartIndex, 1 - positionOffset );
                        mObserver.onNext( mNextIndex, -positionOffset );
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
                        if( mNextIndex == mPager.getItemCount() ) {
                              mNextIndex = 0;
                        }
                        mObserver.onCurrent( mStartIndex, -positionOffset );
                        mObserver.onNext( mNextIndex, 1 - positionOffset );
                  } else {
                        isLeft = false;
                        mNextIndex = position;
                        if( mStartIndex == 0 ) {
                              mNextIndex = mPager.getItemCount() - 1;
                        }
                        mObserver.onCurrent( mStartIndex, 1 - positionOffset );
                        mObserver.onNext( mNextIndex, -positionOffset );
                  }
            }
      }

      @Override
      public void onPageSelected ( int position ) {

            mObserver.onPageSelected( position );
      }

      @Override
      public void onPageScrollStateChanged ( int state ) {

            mCurrentState = state;
            if( state == ViewPager.SCROLL_STATE_DRAGGING || state == ViewPager.SCROLL_STATE_IDLE ) {
                  mStartIndex = mPager.getCurrentItemPosition();
            }
      }
}
