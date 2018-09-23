package tech.threekilogram.pager.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import java.lang.reflect.Field;

/**
 * @author LiuJin
 * @date 2017-12-25
 */
public class ExtendViewPager extends ViewPager {

      /**
       * 将viewPager的scroller替换使其可以增加滚动时间
       */
      protected DurationChangedScroller mScroller;

      /**
       * 入过为true,表示调用了{@link #requestLayout()}
       */
      protected boolean mCallSuperRequestLayout;

      public ExtendViewPager ( Context context ) {

            this( context, null );
      }

      public ExtendViewPager ( Context context, AttributeSet attrs ) {

            super( context, attrs );
            init();
      }

      protected void init ( ) {

            //反射获得 scroller
            attachViewPagerScroller();
      }

      @Override
      public void requestLayout ( ) {

            mCallSuperRequestLayout = true;
            super.requestLayout();
            mCallSuperRequestLayout = false;
      }

      public void setCurrentItem ( int item, boolean smoothScroll, int scrollDurationAdded ) {

            if( mScroller != null ) {
                  mScroller.setDurationAdded( scrollDurationAdded );
            }
            super.setCurrentItem( item, smoothScroll );
            if( mScroller != null ) {
                  mScroller.setDurationAdded( 0 );
            }
      }

      public void smoothScrollToNextItem ( ) {

            setCurrentItem( getCurrentItem() + 1, true );
      }

      public void smoothScrollToNextItem ( int scrollDurationAdded ) {

            setCurrentItem( getCurrentItem() + 1, true, scrollDurationAdded );
      }

      public void smoothScrollToPrevItem ( ) {

            setCurrentItem( getCurrentItem() - 1, true );
      }

      public void smoothScrollToPrevItem ( int scrollDurationAdded ) {

            setCurrentItem( getCurrentItem() - 1, true, scrollDurationAdded );
      }

      public boolean isCallSuperRequestLayout ( ) {

            return mCallSuperRequestLayout;
      }

      /**
       * @param b {@link #setClipChildren(boolean)} 和 {@link #setClipToPadding(boolean)} 都将设置为 b
       */
      public void setClip ( boolean b ) {

            setClipChildren( b );
            setClipToPadding( b );
      }

      public void abortScroller ( ) {

            if( mScroller != null && !mScroller.isFinished() ) {
                  mScroller.abortAnimation();
            }
      }

      /**
       * 将viewPager的scroller设置为可增加时间scroller,并且持有它,用于以后更改滚动时间
       */
      private void attachViewPagerScroller ( ) {

            try {
                  Field scrollerInPager = ViewPager.class.getDeclaredField( "mScroller" );
                  scrollerInPager.setAccessible( true );

                  Field interpolator = ViewPager.class.getDeclaredField( "sInterpolator" );
                  interpolator.setAccessible( true );

                  mScroller = new DurationChangedScroller(
                      this.getContext(), (Interpolator) interpolator.get( null ) );

                  scrollerInPager.set( this, mScroller );
            } catch(NoSuchFieldException e) {

                  e.printStackTrace();
            } catch(IllegalAccessException e) {

                  e.printStackTrace();
            }
      }
}