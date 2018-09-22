package tech.threekilogram.pager.observer;

import android.support.v4.view.ViewPager;

/**
 * Created by LiuJin on 2017-12-30:16:38
 *
 * @author wuxio
 */
public interface OnPagerScrollListener {

      /**
       * 当前正在显示页面滚动情况
       *
       * @param currentPosition 滚动时当前item position,{@link ViewPager#getCurrentItem()}
       * @param offset 滚动进度,向正方向(一般是向左)滚动时,offset从0变化为-1,向负方向(一般是向右)滚动时,offset从0变化为1
       */
      void onCurrent ( int currentPosition, float offset );

      /**
       * 下一个将要显示的页面滚动情况
       *
       * @param nextPosition 滚动时下一个将要显示的item position,和滚动方向相关
       * @param offset 滚动进度,向正方向(一般是向左)滚动时,nextPosition是当前item的下一个item,offset从1变化为0,
       *     向负方向(一般是向右)滚动时,nextPosition是当前item的上一个item,offset从-1变化为0
       */
      void onNext ( int nextPosition, float offset );

      /**
       * 当页面选定时回调
       *
       * @param position 选中的位置
       */
      void onPageSelected ( int position );
}
