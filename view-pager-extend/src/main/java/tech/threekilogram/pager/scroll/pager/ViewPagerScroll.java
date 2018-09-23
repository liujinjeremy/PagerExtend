package tech.threekilogram.pager.scroll.pager;

import android.support.v4.view.ViewPager;

/**
 * 为viewpager增加一个滚动观察者,可以观察当前页面和下一个将要显示的页面的滚动进度及方向,主要用来为Pager设置指示器
 * Created by LiuJin on 2017-12-31:11:37
 *
 * @author wuxio
 */

public class ViewPagerScroll {

      /**
       * pager
       */
      private ViewPager               mPager;
      /**
       * 连接{@link #mPager}和{@link OnViewPagerScrollListener}
       */
      private ViewPagerScrollListener mViewPagerScrollListener;

      /**
       * 根据一个viewpager创建一个滚动观察者
       */
      public ViewPagerScroll ( ViewPager pager ) {

            mPager = pager;
      }

      /**
       * 设置滚动监听,当为null时,将会清除监听
       */
      public void setOnPagerScrollListener ( OnViewPagerScrollListener listener ) {

            if( mViewPagerScrollListener == null ) {
                  mViewPagerScrollListener = new ViewPagerScrollListener( mPager );
            }
            mViewPagerScrollListener.setOnViewPagerScrollListener( listener );

            /* 防止重复添加监听 */
            mPager.removeOnPageChangeListener( mViewPagerScrollListener );
            mPager.addOnPageChangeListener( mViewPagerScrollListener );
      }

      public static String stateToString ( int state ) {

            String result = null;
            if( state == ViewPager.SCROLL_STATE_IDLE ) {
                  result = "SCROLL_STATE_IDLE";
            } else if( state == ViewPager.SCROLL_STATE_DRAGGING ) {
                  result = "SCROLL_STATE_DRAGGING";
            } else if( state == ViewPager.SCROLL_STATE_SETTLING ) {
                  result = "SCROLL_STATE_SETTLING";
            }

            return result;
      }
}