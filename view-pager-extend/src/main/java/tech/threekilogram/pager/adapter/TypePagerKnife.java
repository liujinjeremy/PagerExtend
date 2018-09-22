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
class TypePagerKnife {

      private ArrayList<PagerItemInfo> mItemsInfo;

      private SparseArray<ArrayList<PagerItemInfo>> mTypeItems;

      private BaseTypePagerAdapter mPagerAdapter;

      TypePagerKnife ( BaseTypePagerAdapter pagerAdapter ) {

            mPagerAdapter = pagerAdapter;
            mTypeItems = new SparseArray<>();
      }

      @SuppressWarnings("unchecked")
      public PagerItemInfo getPagerItemInfo ( ViewGroup container, int position ) {

            int type = mPagerAdapter.getViewType( position );
            mItemsInfo = mTypeItems.get( type );
            if( mItemsInfo == null ) {
                  mItemsInfo = new ArrayList<>();
                  mTypeItems.put( type, mItemsInfo );
            }

            PagerItemInfo info = null;
            for( int i = 0; i < mItemsInfo.size(); i++ ) {
                  PagerItemInfo infoNew = mItemsInfo.get( i );
                  if( infoNew.getPosition() == -1 ) {
                        info = infoNew;
                        break;
                  }
            }
            if( info == null ) {
                  info = new PagerItemInfo();
                  mItemsInfo.add( info );
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
            mItemsInfo = mTypeItems.get( type );
            if( mItemsInfo == null ) {
                  return null;
            }

            int size = mItemsInfo.size();
            for( int i = 0; i < size; i++ ) {
                  PagerItemInfo item = mItemsInfo.get( i );
                  if( item.getPosition() == position ) {
                        return item;
                  }
            }

            return null;
      }
}