package tech.threekilogram.pager.scroll.recycler;

/**
 * Created by LiuJin on 2017-12-30:16:38
 *
 * @author wuxio
 */
public interface OnRecyclerPagerScrollListener {

      /**
       * 当前正在显示页面滚动情况
       *
       * @param state scroll state
       * @param currentPosition 当前页面位置
       * @param nextPosition 下一个页面位置
       * @param offsetX x方向滚动距离
       * @param offsetY y方向滚动距离
       */
      void onScroll ( int state, int currentPosition, int nextPosition, int offsetX, int offsetY );

      /**
       * 当页面选定时回调
       *
       * @param prevSelected 之前选中的页面位置
       * @param newSelected 现在选中的页面位置
       */
      void onPageSelected ( int prevSelected, int newSelected );
}
