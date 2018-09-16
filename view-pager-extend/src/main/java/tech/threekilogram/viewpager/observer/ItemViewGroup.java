package tech.threekilogram.viewpager.observer;

/**
 * Created by LiuJin on 2017-12-31:9:03
 * item容器,用于{@link OnPageChangeScrollListener}
 *
 * @author wuxio
 */
public interface ItemViewGroup {

      /**
       * 当前item 位置
       *
       * @return 当前item 位置
       */
      int getCurrentItemPosition ( );

      /**
       * item 总数
       *
       * @return item 总数
       */
      int getItemCount ( );
}
