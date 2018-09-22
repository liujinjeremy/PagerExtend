package tech.threekilogram.viewpager.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import tech.threekilogram.viewpager.pager.ExtendViewPager;

/**
 * @author Liujin 2018-09-22:8:27
 */
public class ViewPagerBanner extends LoopHandlerLayout {

      protected ViewPager mViewPager;

      public ViewPagerBanner ( @NonNull Context context ) {

            this( context, null, 0 );
      }

      public ViewPagerBanner (
          @NonNull Context context,
          @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public ViewPagerBanner (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init();
      }

      @Override
      protected void init ( ) {

            super.init();
      }

      @Override
      protected void onFinishInflate ( ) {

            super.onFinishInflate();

            setClip( false );

            mViewPager = new ExtendViewPager( getContext() );
            addView( mViewPager, 0 );
      }

      public ViewPager getViewPager ( ) {

            return mViewPager;
      }

      public void setPagerAdapter ( PagerAdapter adapter ) {

            mViewPager.setAdapter( adapter );
            if( mOnLoopListener == null || !( mOnLoopListener instanceof BannerOnLoopListener ) ) {
                  mOnLoopListener = new BannerOnLoopListener();
            }
            setOnLoopListener( mOnLoopListener );
      }

      public PagerAdapter getPagerAdapter ( ) {

            return mViewPager.getAdapter();
      }

      public int getCount ( ) {

            PagerAdapter adapter = mViewPager.getAdapter();
            return adapter == null ? 0 : adapter.getCount();
      }

      private class BannerOnLoopListener implements OnLoopListener {

            @Override
            public void onLoop ( LoopHandlerLayout layout ) {

                  int currentItem = mViewPager.getCurrentItem();
                  int i = currentItem + 1;
                  if( i >= getCount() ) {
                        stopLoop();
                        return;
                  }
                  mViewPager.setCurrentItem( i, true );
            }
      }
}
