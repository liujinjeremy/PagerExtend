package tech.liujin.pager.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LiuJin on 2018-01-01:16:31
 * 此类优化原始 {@link PagerAdapter},提供简单API,并且自动管理,View的复用,
 * 当数据修改时,快速响应到界面,如果需要使用多类型的view展示数据,使用 {@link BaseTypePagerAdapter}
 *
 * @author wuxio
 */
public abstract class BasePagerAdapter<D, V extends View> extends PagerAdapter {

      private PagerItemPool<D, V> mPool;

      public BasePagerAdapter ( ) {

            mPool = new PagerItemPool<>( this );
      }

      @NonNull
      @Override
      public Object instantiateItem ( @NonNull ViewGroup container, int position ) {

            PagerItemInfo<D, V> info = mPool.getPagerItemInfo( container, position );
            container.addView( info.getView() );
            return info;
      }

      /**
       * item 数量
       *
       * @return item数量
       */
      @Override
      public abstract int getCount ( );

      @Override
      public boolean isViewFromObject ( @NonNull View view, @NonNull Object object ) {

            PagerItemInfo pagerItemInfo = (PagerItemInfo) object;
            return pagerItemInfo.getView() == view;
      }

      @SuppressWarnings("unchecked")
      @Override
      public void destroyItem (
          @NonNull ViewGroup container, int position, @NonNull Object object ) {

            PagerItemInfo<D, V> itemInfo = (PagerItemInfo<D, V>) object;
            View view = itemInfo.getView();
            container.removeView( view );
            itemInfo.setPosition( -1 );
            itemInfo.setData( null );
      }

      @SuppressWarnings("unchecked")
      @Override
      public int getItemPosition ( @NonNull Object object ) {

            return PagerAdapter.POSITION_NONE;
      }

      /**
       * 返回该位置的数据,每次{@link PagerAdapter#instantiateItem(ViewGroup, int)}都会回调该方法
       *
       * @param position item位置
       *
       * @return item位置对应的数据
       */
      protected abstract D getData ( int position );

      /**
       * 返回该位置的view,
       * {@link PagerAdapter#instantiateItem(ViewGroup, int)}不一定会回调该方法,只有没有缓存的view的时候回调
       *
       * @param container container
       * @param position item 位置
       *
       * @return item处的view
       */
      protected abstract V getView ( ViewGroup container, int position );

      /**
       * 该方法决定数据如何绑定到view上,每次{@link PagerAdapter#instantiateItem(ViewGroup, int)}都会回调该方法,
       * 在{@link #getData(int)}之后回调
       *
       * @param position item位置
       * @param data 数据{@link #getData(int)}获取的数据
       * @param view item处的view {@link #getView(ViewGroup, int)}获取的view
       */
      protected abstract void bindData ( int position, D data, V view );

      /**
       * get view at position
       *
       * @param position position
       *
       * @return item view
       */
      public V getItemView ( int position ) {

            PagerItemInfo<D, V> item = mPool.getPagerItem( position );
            return item == null ? null : item.getView();
      }

      /**
       * get data at position
       *
       * @param position position
       *
       * @return data
       */
      public D getItemData ( int position ) {

            PagerItemInfo<D, V> item = mPool.getPagerItem( position );
            return item == null ? null : item.getData();
      }
}