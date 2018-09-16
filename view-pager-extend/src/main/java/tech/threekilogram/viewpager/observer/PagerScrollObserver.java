package tech.threekilogram.viewpager.observer;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 为viewpager增加一个滚动观察者,可以观察当前页面和下一个将要显示的页面的滚动进度及方向,主要用来为Pager设置指示器
 * Created by LiuJin on 2017-12-31:11:37
 *
 * @author wuxio
 */

public class PagerScrollObserver {

      private ViewPager      mPager;
      private ItemViewGroup  mItemViewGroup;
      private ScrollObserver mScrollObserver;

      public static PagerScrollObserver from ( ViewPager pager ) {

            return new PagerScrollObserver( pager );
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
}