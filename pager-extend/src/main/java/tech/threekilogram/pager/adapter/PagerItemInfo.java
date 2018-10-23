package tech.threekilogram.pager.adapter;

import android.view.View;

/**
 * Created by LiuJin on 2018-01-01:12:21
 * 记录 pager 每个 item 信息
 *
 * @author wuxio
 */
class PagerItemInfo<D, V extends View> {

      /**
       * 位置
       */
      private int position;
      /**
       * 数据
       */
      private D   mData;
      /**
       * view
       */
      private V   mView;

      public int getPosition ( ) {

            return position;
      }

      void setPosition ( int position ) {

            this.position = position;
      }

      public D getData ( ) {

            return mData;
      }

      void setData ( D data ) {

            mData = data;
      }

      public V getView ( ) {

            return mView;
      }

      void setView ( V view ) {

            mView = view;
      }

      @Override
      public String toString ( ) {

            return "PagerItemInfo{" +
                "position=" + position +
                ", mData=" + mData +
                ", mView=" + mView +
                '}';
      }
}
