package tech.threekilogram.pager.adapter;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * Created by LiuJin on 2018-01-01:13:23
 * 为{@link BasePagerAdapter}管理{@link PagerItemInfo}
 *
 * @author wuxio
 */
class PagerItemPool<D, V extends View> {

      private ArrayList<PagerItemInfo<D, V>> mItemsInfo = new ArrayList<>();

      private BasePagerAdapter<D, V> mPagerAdapter;

      PagerItemPool ( BasePagerAdapter<D, V> pagerAdapter ) {

            mPagerAdapter = pagerAdapter;
      }

      @SuppressWarnings("unchecked")
      public PagerItemInfo<D, V> getPagerItemInfo ( ViewGroup container, int position ) {

            PagerItemInfo<D, V> itemInfo = null;

            /* find out a not null info */
            ArrayList<PagerItemInfo<D, V>> itemsInfo = mItemsInfo;
            for( int i = 0; i < itemsInfo.size(); i++ ) {
                  PagerItemInfo infoNew = itemsInfo.get( i );
                  if( infoNew.getPosition() == -1 ) {
                        itemInfo = infoNew;
                        break;
                  }
            }
            if( itemInfo == null ) {
                  itemInfo = new PagerItemInfo<>();
                  itemsInfo.add( itemInfo );
            }
            itemInfo.setPosition( position );

            D data = mPagerAdapter.getData( position );
            itemInfo.setData( data );
            V view = itemInfo.getView();
            if( view == null ) {
                  view = mPagerAdapter.getView( container, position );
                  itemInfo.setView( view );
            }
            mPagerAdapter.bindData( position, data, view );

            return itemInfo;
      }

      public PagerItemInfo<D, V> getPagerItem ( int position ) {

            int size = mItemsInfo.size();
            for( int i = 0; i < size; i++ ) {
                  PagerItemInfo<D, V> info = mItemsInfo.get( i );
                  if( info.getPosition() == position ) {
                        return info;
                  }
            }
            return null;
      }
}