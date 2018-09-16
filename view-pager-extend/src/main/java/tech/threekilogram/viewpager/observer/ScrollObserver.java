package tech.threekilogram.viewpager.observer;

import android.support.v4.view.ViewPager;

/**
 * Created by LiuJin on 2017-12-31:8:45
 * viewPager滚动时的观察者,滚动时会通知 OnPagerScrollObserver 当前滚动的position,下个将要显示的position,以及进度值
 */
class ScrollObserver implements ViewPager.OnPageChangeListener {

      private ItemViewGroup         mPager;
      private OnPagerScrollObserver mObserver;

      private int     startIndex;
      private int     currentState;
      private int     nextIndex;
      private boolean isLeft;

      ScrollObserver ( ItemViewGroup pager, OnPagerScrollObserver observer ) {

            mPager = pager;
            mObserver = observer;
      }

      void setObserver ( OnPagerScrollObserver observer ) {

            mObserver = observer;
      }

      OnPagerScrollObserver getObserver ( ) {

            return mObserver;
      }

      @Override
      public void onPageScrolled ( int position, float positionOffset, int positionOffsetPixels ) {

            if( mObserver == null ) {
                  return;
            }

            if( currentState == ViewPager.SCROLL_STATE_SETTLING ) {

                  final float endFlag = 0.f;

                  if( positionOffset == endFlag ) {
                        int itemPosition = mPager.getCurrentItemPosition();

                        if( isLeft ) {
                              if( startIndex != itemPosition ) {
                                    mObserver.onCurrent( startIndex, -1.f );
                                    mObserver.onNext( nextIndex, 0.f );
                              } else {
                                    mObserver.onCurrent( startIndex, 0.f );
                                    mObserver.onNext( nextIndex, 1.f );
                              }
                        } else {
                              if( startIndex != itemPosition ) {
                                    mObserver.onCurrent( startIndex, 1.f );
                                    mObserver.onNext( nextIndex, 0.f );
                              } else {
                                    mObserver.onCurrent( startIndex, 0.f );
                                    mObserver.onNext( nextIndex, -1.f );
                              }
                        }
                        return;
                  }

                  if( startIndex == position ) {
                        isLeft = true;
                        nextIndex = startIndex + 1;
                        if( nextIndex == mPager.getItemCount() ) {
                              nextIndex = 0;
                        }
                        mObserver.onCurrent( startIndex, -positionOffset );
                        mObserver.onNext( nextIndex, 1 - positionOffset );
                  } else {
                        isLeft = false;
                        nextIndex = position;
                        if( startIndex == 0 ) {
                              nextIndex = mPager.getItemCount() - 1;
                        }
                        mObserver.onCurrent( startIndex, 1 - positionOffset );
                        mObserver.onNext( nextIndex, -positionOffset );
                  }
            }

            if( currentState == ViewPager.SCROLL_STATE_DRAGGING ) {

                  final float endFlag = 0.f;

                  if( positionOffset == endFlag ) {
                        return;
                  }

                  if( startIndex == position ) {
                        isLeft = true;
                        nextIndex = startIndex + 1;
                        if( nextIndex == mPager.getItemCount() ) {
                              nextIndex = 0;
                        }
                        mObserver.onCurrent( startIndex, -positionOffset );
                        mObserver.onNext( nextIndex, 1 - positionOffset );
                  } else {
                        isLeft = false;
                        nextIndex = position;
                        if( startIndex == 0 ) {
                              nextIndex = mPager.getItemCount() - 1;
                        }
                        mObserver.onCurrent( startIndex, 1 - positionOffset );
                        mObserver.onNext( nextIndex, -positionOffset );
                  }
            }
      }

      @Override
      public void onPageSelected ( int position ) {

      }

      @Override
      public void onPageScrollStateChanged ( int state ) {

            currentState = state;
            if( state == ViewPager.SCROLL_STATE_DRAGGING || state == ViewPager.SCROLL_STATE_IDLE ) {
                  startIndex = mPager.getCurrentItemPosition();
            }
      }
}
