package tech.threekilogram.pager.scroll.pager;

import android.support.v4.view.ViewPager;

/**
 * Created by LiuJin on 2017-12-30:16:38
 *
 * @author wuxio
 */
public interface OnViewPagerScrollListener {

      /**
       * 当前正在显示页面滚动情况
       *
       * @param state 当前状态
       * @param currentPosition 滚动时当前item position,{@link ViewPager#getCurrentItem()}
       * @param nextPosition 下一个item position,与滚动方向有关
       * @param offset 滚动进度,向正方向(一般是向左)滚动时,offset从0变化为-1,向负方向(一般是向右)滚动时,offset从0变化为1
       * @param offsetPix 滚动距离
       */
      void onScroll (
          int state, int currentPosition, int nextPosition, float offset, int offsetPix );

      /**
       * 当页面选定时回调
       *
       * @param prevSelected 之前选中的页面位置
       * @param newSelected 现在选中的页面位置
       */
      void onPageSelected ( int prevSelected, int newSelected );
}
