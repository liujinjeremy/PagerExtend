package tech.threekilogram.viewpager.observer;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import tech.threekilogram.viewpager.adapter.MaxCountAdapter;

/**
 * 为viewpager增加一个滚动观察者,可以观察当前页面和下一个将要显示的页面的滚动进度及方向,主要用来为Pager设置指示器
 * Created by LiuJin on 2017-12-31:11:37
 *
 * @author wuxio
 */

public class PagerScrollObserver {

      private ItemViewGroup              mItemViewGroup;
      private ViewPager                  mPager;
      private OnPageChangeScrollListener mScrollListener;

      /**
       * 根据一个viewpager创建一个滚动观察者
       */
      public static PagerScrollObserver from ( ViewPager pager ) {

            return new PagerScrollObserver( pager );
      }

      /**
       * 根据一个viewpager创建一个滚动观察者,如果viewpager使用的的是{@link MaxCountAdapter},需要转换一下位置
       */
      public static PagerScrollObserver from (
          final ViewPager pager,
          final MaxCountAdapter adapter ) {

            return new PagerScrollObserver( pager, new ItemViewGroup() {

                  @Override
                  public int getCurrentItemPosition ( ) {

                        int currentItem = pager.getCurrentItem();
                        return adapter.getAdapterPosition( currentItem );
                  }

                  @Override
                  public int getItemCount ( ) {

                        return adapter.getAdapterCount();
                  }
            } );
      }

      /**
       * 设置滚动监听
       */
      public void setOnScrollObserver ( @Nullable OnPagerScrollObserver observer ) {

            if( mScrollListener != null ) {
                  mPager.removeOnPageChangeListener( mScrollListener );
            }
            mScrollListener = new OnPageChangeScrollListener( mItemViewGroup, observer );
            mPager.addOnPageChangeListener( mScrollListener );
      }

      public OnPageChangeScrollListener getOnPagerScrollObserver ( ) {

            return mScrollListener;
      }

      private PagerScrollObserver ( final ViewPager pager ) {

            mPager = pager;

            mItemViewGroup = new ItemViewGroup() {

                  @Override
                  public int getCurrentItemPosition ( ) {

                        return pager.getCurrentItem();
                  }

                  @Override
                  public int getItemCount ( ) {

                        PagerAdapter adapter = pager.getAdapter();
                        if( adapter != null ) {
                              return adapter.getCount();
                        }
                        return 0;
                  }
            };
      }

      private PagerScrollObserver ( final ViewPager pager, ItemViewGroup itemViewGroup ) {

            mPager = pager;
            mItemViewGroup = itemViewGroup;
      }
}