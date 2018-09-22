package tech.threekilogram.viewpager.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import tech.threekilogram.viewpager.adapter.MaxCountAdapter;
import tech.threekilogram.viewpager.pager.ExtendViewPager;

/**
 * @author Liujin 2018-09-22:8:27
 */
public class ViewPagerBanner extends LoopHandlerLayout {

      private static final String TAG = ViewPagerBanner.class.getSimpleName();

      /**
       * 展示界面的pager
       */
      protected ExtendViewPager mViewPager;
      /**
       * viewpager自动轮播时额外增加的滚动时间
       */
      protected int mScrollDurationAdded = 1000;
      /**
       * 监听变化
       */
      protected BannerPagerChangeListener mPagerChangeListener;

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

            /* 不裁剪children */
            setClip( false );

            /* 添加pager */
            mViewPager = createPager( getContext() );
            addView( mViewPager, 0 );
      }

      protected ExtendViewPager createPager ( Context context ) {

            return new ExtendViewPager( context );
      }

      @Override
      public boolean dispatchTouchEvent ( MotionEvent ev ) {

            /* 按下暂停,抬起继续 */
            onTouchPauseLoop( ev );
            return super.dispatchTouchEvent( ev );
      }

      /**
       * 设置自动轮播时滚动时间增加
       *
       * @param scrollDurationAdded 新时间
       */
      public void setScrollDurationAdded ( int scrollDurationAdded ) {

            mScrollDurationAdded = scrollDurationAdded;
      }

      /**
       * 获取设置的自动轮播时滚动时间增加
       */
      public int getScrollDurationAdded ( ) {

            return mScrollDurationAdded;
      }

      /**
       * 为{@link #mViewPager}设置Adapter
       */
      public void setBannerAdapter ( PagerAdapter adapter ) {

            MaxCountAdapter maxCountAdapter = new MaxCountAdapter( adapter );
            mViewPager.setAdapter( maxCountAdapter );
            mViewPager.setCurrentItem( maxCountAdapter.getStartPosition() );

            if( mOnLoopListener == null || !( mOnLoopListener instanceof BannerOnLoopListener ) ) {
                  mOnLoopListener = new BannerOnLoopListener();
            }
            setOnLoopListener( mOnLoopListener );
      }

      /**
       * 获取{@link #mViewPager}设置的Adapter
       */
      public PagerAdapter getBannerAdapter ( ) {

            MaxCountAdapter adapter = (MaxCountAdapter) mViewPager.getAdapter();
            return adapter == null ? null : adapter.getPagerAdapter();
      }

      /**
       * 获取数据总数
       */
      public int getItemCount ( ) {

            MaxCountAdapter adapter = (MaxCountAdapter) mViewPager.getAdapter();
            return adapter == null ? 0 : adapter.getAdapterCount();
      }

      /**
       * 获取当前条目
       */
      public int getCurrentItem ( ) {

            int currentItem = mViewPager.getCurrentItem();
            MaxCountAdapter adapter = (MaxCountAdapter) mViewPager.getAdapter();
            return adapter == null ? currentItem : adapter.getAdapterPosition( currentItem );
      }

      /**
       * 获取条目的adapter位置
       */
      public int getItemAdapterPosition ( int position ) {

            MaxCountAdapter adapter = (MaxCountAdapter) mViewPager.getAdapter();
            if( adapter != null ) {
                  adapter.getAdapterPosition( position );
            }
            return -1;
      }

      public void setCurrentItem ( int item ) {

            mViewPager.setCurrentItem( item );
      }

      /**
       * 添加监听
       */
      public void addOnPageChangeListener ( @NonNull OnPageChangeListener listener ) {

            if( mPagerChangeListener == null ) {
                  mPagerChangeListener = new BannerPagerChangeListener();
                  mViewPager.setOnPageChangeListener( mPagerChangeListener );
            }

            mPagerChangeListener.add( listener );
      }

      /**
       * 删除监听
       */
      public void removeOnPageChangeListener ( @NonNull OnPageChangeListener listener ) {

            if( mPagerChangeListener != null ) {
                  mPagerChangeListener.remove( listener );
            }
      }

      /**
       * 清除所有监听
       */
      public void clearOnPageChangeListeners ( ) {

            if( mPagerChangeListener != null ) {
                  mPagerChangeListener.clear();
            }
      }

      /**
       * 设置页面变换
       */
      public void setPageTransformer (
          boolean reverseDrawingOrder,
          @Nullable PageTransformer transformer ) {

            mViewPager.setPageTransformer( reverseDrawingOrder, transformer );
      }

      /**
       * Set the margin between pages.
       *
       * @param marginPixels Distance between adjacent pages in pixels
       *
       * @see #getPageMargin()
       * @see #setPageMarginDrawable(Drawable)
       * @see #setPageMarginDrawable(int)
       */
      public void setPageMargin ( int marginPixels ) {

            mViewPager.setPageMargin( marginPixels );
      }

      /**
       * Return the margin between pages.
       *
       * @return The size of the margin in pixels
       */
      public int getPageMargin ( ) {

            return mViewPager.getPageMargin();
      }

      /**
       * Set a drawable that will be used to fill the margin between pages.
       *
       * @param d Drawable to display between pages
       */
      public void setPageMarginDrawable ( @Nullable Drawable d ) {

            mViewPager.setPageMarginDrawable( d );
      }

      /**
       * Set a drawable that will be used to fill the margin between pages.
       *
       * @param resId Resource ID of a drawable to display between pages
       */
      public void setPageMarginDrawable ( @DrawableRes int resId ) {

            setPageMarginDrawable( ContextCompat.getDrawable( getContext(), resId ) );
      }

      /**
       * 设置轮播行为
       */
      private class BannerOnLoopListener implements OnLoopListener {

            @Override
            public void onLoop ( LoopHandlerLayout layout ) {

                  mViewPager.smoothScrollToNextItem( mScrollDurationAdded );
            }
      }

      private class BannerPagerChangeListener implements OnPageChangeListener {

            private ArrayList<OnPageChangeListener> mOnPageChangeListeners = new ArrayList<>();

            private void add ( OnPageChangeListener listener ) {

                  mOnPageChangeListeners.add( listener );
            }

            private void remove ( OnPageChangeListener listener ) {

                  mOnPageChangeListeners.remove( listener );
            }

            private void clear ( ) {

                  mOnPageChangeListeners.clear();
            }

            @Override
            public void onPageScrolled (
                int position, float positionOffset, int positionOffsetPixels ) {

                  MaxCountAdapter adapter = (MaxCountAdapter) mViewPager.getAdapter();
                  if( adapter != null ) {

                        position = adapter.getAdapterPosition( position );
                        for( OnPageChangeListener onPageChangeListener : mOnPageChangeListeners ) {
                              onPageChangeListener
                                  .onPageScrolled( position, positionOffset, positionOffsetPixels );
                        }
                  }
            }

            @Override
            public void onPageSelected ( int position ) {

                  MaxCountAdapter adapter = (MaxCountAdapter) mViewPager.getAdapter();
                  if( adapter != null ) {

                        position = adapter.getAdapterPosition( position );
                        Log.e( TAG, "onPageSelected : " + position );
                        for( OnPageChangeListener onPageChangeListener : mOnPageChangeListeners ) {
                              onPageChangeListener.onPageSelected( position );
                        }
                  }
            }

            @Override
            public void onPageScrollStateChanged ( int state ) {

                  for( OnPageChangeListener onPageChangeListener : mOnPageChangeListeners ) {
                        onPageChangeListener.onPageScrollStateChanged( state );
                  }
            }
      }
}
