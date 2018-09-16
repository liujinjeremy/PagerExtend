package tech.threekilogram.viewpager.observer;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 为viewpager增加一个滚动观察者,可以观察当前页面和下一个将要显示的页面的滚动进度及方向,主要用来为Pager设置指示器
 * Created by LiuJin on 2017-12-31:11:37
 *
 * @author wuxio
 */

public class PagerScroll {

      /**
       * pager
       */
      private ViewPager                  mPager;
      /**
       * 获取pager item position 状态
       */
      private ItemViewGroup              mItemViewGroup;
      /**
       * 连接{@link #mPager}和{@link OnPagerScrollListener}
       */
      private OnPageChangeScrollListener mOnPageChangeScrollListener;

      /**
       * 根据一个viewpager创建一个滚动观察者
       */
      public PagerScroll ( final ViewPager pager ) {

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

      /**
       * 设置滚动监听,当为null时,将会清除监听
       */
      public void setOnPagerScrollListener ( OnPagerScrollListener listener ) {

            if( mOnPageChangeScrollListener == null ) {
                  mOnPageChangeScrollListener = new OnPageChangeScrollListener( mItemViewGroup );
            }
            mOnPageChangeScrollListener.setOnPagerScrollListener( listener );

            /* 防止重复添加监听 */
            mPager.removeOnPageChangeListener( mOnPageChangeScrollListener );
            mPager.addOnPageChangeListener( mOnPageChangeScrollListener );
      }

      public OnPagerScrollListener getOnPagerScrollListener ( ) {

            return mOnPageChangeScrollListener == null ? null
                : mOnPageChangeScrollListener.getOnPagerScrollListener();
      }
}