package tech.threekilogram.viewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;
import tech.threekilogram.viewpager.adapter.MaxCountAdapter;
import tech.threekilogram.viewpager.observer.OnPagerScrollListener;
import tech.threekilogram.viewpager.observer.PagerScroll;

/**
 * @author LiuJin
 * @date 2017-12-25
 *     轮播图,包含一个LoopViewPager,也可以配置指示器
 */
public class BannerView extends FrameLayout {

      private ExtendViewPager            mViewPager;
      private MaxCountAdapter            mMaxCountAdapter;
      private BannerOnPageChangeListener mOnPagerScrollListener;
      private LoopHandler                mLoopHandler;
      private boolean                    isAutoLoop;
      private int                        mLoopTime;
      private PagerScroll                mPagerScroll;

      public BannerView ( @NonNull Context context ) {

            this( context, null );
      }

      public BannerView ( @NonNull Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public BannerView (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init();
      }

      private void init ( ) {

            mViewPager = new ExtendViewPager( getContext() );
            mOnPagerScrollListener = new BannerOnPageChangeListener();
            mLoopHandler = new LoopHandler( this );
      }

      @Override
      protected void onFinishInflate ( ) {

            super.onFinishInflate();

            addView( mViewPager, 0 );
            mViewPager.addOnPageChangeListener( mOnPagerScrollListener );

            //设为false支持pager裁剪
            setClipChildren( false );
            setClipToPadding( false );
            mViewPager.setClip( false );
      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent ev ) {

            mLoopHandler.clearLoopToNextAtDelayed();

            int action = ev.getAction();
            if( action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_OUTSIDE
                || action == MotionEvent.ACTION_CANCEL ) {

                  if( isAutoLoop ) {
                        mLoopHandler.loopToNextAtDelayed( mLoopTime );
                  }
            }

            return super.dispatchTouchEvent( ev );
      }

      @Override
      protected void onDetachedFromWindow ( ) {

            super.onDetachedFromWindow();
            mLoopHandler.removeCallbacksAndMessages( null );
      }

      public ExtendViewPager getViewPager ( ) {

            return mViewPager;
      }

      public void setPagerAdapter ( PagerAdapter pagerAdapter ) {

            mMaxCountAdapter = new MaxCountAdapter( pagerAdapter );
            mViewPager.setAdapter( mMaxCountAdapter );
            mViewPager.setCurrentItem( mMaxCountAdapter.getStartPosition() );
      }

      public PagerAdapter getPagerAdapter ( ) {

            return mMaxCountAdapter == null ? null : mMaxCountAdapter.getPagerAdapter();
      }

      public void addScrollDuration ( int addTime ) {

            mViewPager.setDurationAdded( addTime );
      }

      public void loopToNextPage ( ) {

            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem( currentItem + 1, true );
      }

      public boolean isAutoLoop ( ) {

            return isAutoLoop;
      }

      public void startLoop ( ) {

            startLoop( 2400 );
      }

      public void startLoop ( int loopTime ) {

            if( isAutoLoop ) {
                  mLoopTime = loopTime;
            } else {
                  isAutoLoop = true;
                  mLoopTime = loopTime;
                  mLoopHandler.loopToNextAtDelayed( loopTime );
            }
      }

      public void stopLoop ( ) {

            if( isAutoLoop ) {

                  isAutoLoop = false;
                  mLoopHandler.clearLoopToNextAtDelayed();
            }
      }

      public void setPageMargin ( int unit, float marginSize ) {

            float dimension = TypedValue.applyDimension(
                unit,
                marginSize,
                getResources().getDisplayMetrics()
            );
            setPageMargin( (int) dimension );
      }

      public void setPageMargin ( int marginPixels ) {

            mViewPager.setPageMargin( marginPixels );
      }

      public void setOnPagerScrollObserver ( OnPagerScrollListener onPagerScrollListener ) {

            if( mPagerScroll == null ) {
                  mPagerScroll = new PagerScroll( mViewPager );
            }
            if( onPagerScrollListener == null ) {
                  mPagerScroll.setOnPagerScrollListener( null );
            } else {
                  mPagerScroll.setOnPagerScrollListener(
                      new BannerOnPagerScrollListener( onPagerScrollListener )
                  );
            }
      }

      public OnPagerScrollListener getOnPagerScrollObserver ( ) {

            if( mPagerScroll == null ) {
                  return null;
            }
            return mPagerScroll.getOnPagerScrollListener();
      }

      /**
       * 发送延时消息
       */
      private static class LoopHandler extends Handler {

            private static final int WHAT_LOOP = 1569;

            private WeakReference<BannerView> mRef;
            private boolean                   isCleared;

            LoopHandler ( BannerView banner ) {

                  mRef = new WeakReference<>( banner );
            }

            private void loopToNextAtDelayed ( int delayed ) {

                  sendEmptyMessageDelayed( WHAT_LOOP, delayed );
                  isCleared = false;
            }

            private void clearLoopToNextAtDelayed ( ) {

                  if( isCleared ) {
                        return;
                  }
                  removeMessages( WHAT_LOOP );
                  isCleared = true;
            }

            @Override
            public void handleMessage ( Message msg ) {

                  BannerView bannerView = mRef.get();
                  if( bannerView == null ) {
                        return;
                  }

                  if( msg.what == WHAT_LOOP ) {
                        bannerView.loopToNextPage();
                  }
            }
      }

      /**
       * 连接viewPager
       */
      private class BannerOnPageChangeListener implements ViewPager.OnPageChangeListener {

            BannerOnPageChangeListener ( ) { }

            @Override
            public void onPageScrolled (
                int position, float positionOffset, int positionOffsetPixels ) { }

            @Override
            public void onPageSelected ( int position ) {

                  if( isAutoLoop ) {
                        mLoopHandler.loopToNextAtDelayed( mLoopTime );
                  }
            }

            @Override
            public void onPageScrollStateChanged ( int state ) { }
      }

      /**
       * 观察滚动
       */
      private class BannerOnPagerScrollListener implements OnPagerScrollListener {

            private OnPagerScrollListener mOnPagerScrollListener;

            private BannerOnPagerScrollListener (
                OnPagerScrollListener onPagerScrollListener ) {

                  mOnPagerScrollListener = onPagerScrollListener;
            }

            @Override
            public void onCurrent ( int currentPosition, float offset ) {

                  if( mOnPagerScrollListener != null ) {
                        int position = mMaxCountAdapter.getAdapterPosition( currentPosition );
                        mOnPagerScrollListener.onCurrent( position, offset );
                  }
            }

            @Override
            public void onNext ( int nextPosition, float offset ) {

                  if( mOnPagerScrollListener != null ) {
                        int position = mMaxCountAdapter.getAdapterPosition( nextPosition );
                        mOnPagerScrollListener.onNext( position, offset );
                  }
            }

            @Override
            public void onPageSelected ( int position ) {

                  if( mOnPagerScrollListener != null ) {
                        position = mMaxCountAdapter.getAdapterPosition( position );
                        mOnPagerScrollListener.onPageSelected( position );
                  }
            }
      }
}
