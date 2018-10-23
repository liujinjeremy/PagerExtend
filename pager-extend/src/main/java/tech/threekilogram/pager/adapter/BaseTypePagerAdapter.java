package tech.threekilogram.pager.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LiuJin on 2018-01-01:16:31
 * 可以为viewPager 提供多类型视图
 *
 * @author wuxio
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseTypePagerAdapter extends PagerAdapter {

      private TypePagerItemPool mPools;

      public BaseTypePagerAdapter ( ) {

            mPools = new TypePagerItemPool( this );
      }

      @NonNull
      @Override
      public Object instantiateItem ( @NonNull ViewGroup container, int position ) {

            PagerItemInfo info = mPools.getPagerItemInfo( container, position );
            container.addView( info.getView() );
            return info;
      }

      @Override
      public boolean isViewFromObject ( @NonNull View view, @NonNull Object object ) {

            PagerItemInfo pagerItemInfo = (PagerItemInfo) object;
            return pagerItemInfo.getView() == view;
      }

      @SuppressWarnings("unchecked")
      @Override
      public void destroyItem (
          @NonNull ViewGroup container, int position, @NonNull Object object ) {

            PagerItemInfo itemInfo = (PagerItemInfo) object;
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
       * 返回数据总数
       *
       * @return 数据总数
       *
       * @link ImageWatcherAdapter#getCount()
       */
      @Override
      public abstract int getCount ( );

      /**
       * 返回该位置view的类型
       *
       * @param position item位置
       *
       * @return view的类型
       */
      protected abstract int getViewType ( int position );

      /**
       * 返回该位置的数据,每次{@link PagerAdapter#instantiateItem(ViewGroup, int)}都会回调该方法
       *
       * @param position item位置
       * @param type view类型
       *
       * @return {@link BasePagerAdapter#getData(int)}
       */
      protected abstract Object getData ( int position, int type );

      /**
       * 返回该位置的view,{@link PagerAdapter#instantiateItem(ViewGroup, int)}不一定会回调该方法,只有没有缓存的view的时候回调
       *
       * @param container container
       * @param position item位置
       * @param type item位置的view类型
       *
       * @return 该位置的view
       */
      protected abstract View getView ( ViewGroup container, int position, int type );

      /**
       * 绑定数据,每次{@link PagerAdapter#instantiateItem(ViewGroup, int)}都会回调该方法,
       * 在{@link #getData(int, int)}之后回调
       *
       * @param position 当前位置
       * @param data 数据
       * @param view view
       * @param type view 类型
       */
      protected abstract void bindData ( int position, Object data, View view, int type );

      /**
       * get view at position
       *
       * @param position position
       *
       * @return item view
       */
      public View getItemView ( int position ) {

            PagerItemInfo item = mPools.getPagerItem( position );
            return item == null ? null : item.getView();
      }

      /**
       * get data at position
       *
       * @param position position
       *
       * @return data
       */
      public Object getItemData ( int position ) {

            PagerItemInfo item = mPools.getPagerItem( position );
            return item == null ? null : item.getData();
      }
}