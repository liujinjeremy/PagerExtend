package tech.threekilogram.pager.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * Created by LiuJin on 2018-01-01:13:23
 *
 * @author wuxio
 */
class TypePagerItemPool {

      private SparseArray<ArrayList<PagerItemInfo>> mTypeItems;

      private BaseTypePagerAdapter mPagerAdapter;

      TypePagerItemPool ( BaseTypePagerAdapter pagerAdapter ) {

            mPagerAdapter = pagerAdapter;
            mTypeItems = new SparseArray<>();
      }

      @SuppressWarnings("unchecked")
      public PagerItemInfo getPagerItemInfo ( ViewGroup container, int position ) {

            int type = mPagerAdapter.getViewType( position );
            ArrayList<PagerItemInfo> itemInfoList = mTypeItems.get( type );
            if( itemInfoList == null ) {
                  itemInfoList = new ArrayList<>();
                  mTypeItems.put( type, itemInfoList );
            }

            PagerItemInfo info = null;
            for( int i = 0; i < itemInfoList.size(); i++ ) {
                  PagerItemInfo infoNew = itemInfoList.get( i );
                  if( infoNew.getPosition() == -1 ) {
                        info = infoNew;
                        break;
                  }
            }
            if( info == null ) {
                  info = new PagerItemInfo();
                  itemInfoList.add( info );
            }
            info.setPosition( position );

            Object data = mPagerAdapter.getData( position, mPagerAdapter.getViewType( position ) );
            info.setData( data );
            View view = info.getView();
            if( view == null ) {
                  view = mPagerAdapter.getView( container, position, type );
                  info.setView( view );
            }
            mPagerAdapter.bindData( position, data, view, type );

            return info;
      }

      public PagerItemInfo getPagerItem ( int position ) {

            int type = mPagerAdapter.getViewType( position );
            ArrayList<PagerItemInfo> itemsInfoList = mTypeItems.get( type );
            if( itemsInfoList == null ) {
                  return null;
            }

            int size = itemsInfoList.size();
            for( int i = 0; i < size; i++ ) {
                  PagerItemInfo item = itemsInfoList.get( i );
                  if( item.getPosition() == position ) {
                        return item;
                  }
            }

            return null;
      }
}