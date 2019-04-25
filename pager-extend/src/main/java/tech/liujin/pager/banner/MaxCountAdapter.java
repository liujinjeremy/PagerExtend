package tech.liujin.pager.banner;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 此adapter用于包装{@link PagerAdapter}使其可以拥有{@link Integer#MAX_VALUE}条数据,
 * 但实际是通过不断重复数据实现
 *
 * @author wuxio
 */
public class MaxCountAdapter extends PagerAdapter {

      /**
       * 真实adapter
       */
      private PagerAdapter mPagerAdapter;

      public MaxCountAdapter ( PagerAdapter pagerAdapter ) {

            mPagerAdapter = pagerAdapter;
      }

      /**
       * @return {@link Integer#MAX_VALUE}
       */
      @Override
      public int getCount ( ) {

            return Integer.MAX_VALUE;
      }

      /**
       * @return 用户实际adapter的item count
       */
      public int getAdapterCount ( ) {

            return mPagerAdapter.getCount();
      }

      /**
       * @return {@link #getCount()}的中间值,并且是{mPagerAdapter.getAdapterCount()}的倍数,使其正好是实际数据第一项
       */
      public int getStartPosition ( ) {

            int i = Integer.MAX_VALUE / 2;
            if( mPagerAdapter.getCount() == 0 ) {

                  return i;
            } else {

                  return i - ( i % mPagerAdapter.getCount() );
            }
      }

      /**
       * 将loopViewPager的position 转换成 mAdapter的Position
       *
       * @param position MaxCountAdapter 中的位置
       *
       * @return mPagerAdapter 中的位置
       */
      public int getAdapterPosition ( int position ) {

            return position % mPagerAdapter.getCount();
      }

      @Override
      public boolean isViewFromObject ( @NonNull View view, @NonNull Object object ) {

            return mPagerAdapter.isViewFromObject( view, object );
      }

      @NonNull
      @Override
      public Object instantiateItem ( @NonNull ViewGroup container, int position ) {

            position = getAdapterPosition( position );
            return mPagerAdapter.instantiateItem( container, position );
      }

      /**
       * @return 包装的Adapter() ,即用户自己实现的adapter
       */
      public PagerAdapter getPagerAdapter ( ) {

            return mPagerAdapter;
      }

      /*-- 方法签名上带有position的全部重写,更换position为mAdapter对应的position,实现调用mAdapter的实现 --*/

      @Override
      public void destroyItem (
          @NonNull ViewGroup container, int position, @NonNull Object object ) {

            position = getAdapterPosition( position );
            mPagerAdapter.destroyItem( container, position, object );
      }

      @Override
      public void setPrimaryItem (
          @NonNull ViewGroup container, int position, @NonNull Object object ) {

            position = getAdapterPosition( position );
            mPagerAdapter.setPrimaryItem( container, position, object );
      }

      @Override
      public CharSequence getPageTitle ( int position ) {

            position = getAdapterPosition( position );
            return mPagerAdapter.getPageTitle( position );
      }

      @Override
      public float getPageWidth ( int position ) {

            position = getAdapterPosition( position );
            return mPagerAdapter.getPageWidth( position );
      }

      @Override
      public void startUpdate ( @NonNull ViewGroup container ) {

            mPagerAdapter.startUpdate( container );
      }

      @Override
      public void finishUpdate ( @NonNull ViewGroup container ) {

            mPagerAdapter.finishUpdate( container );
      }

      @Override
      public Parcelable saveState ( ) {

            return mPagerAdapter.saveState();
      }

      @Override
      public void restoreState ( Parcelable state, ClassLoader loader ) {

            mPagerAdapter.restoreState( state, loader );
      }

      @Override
      public int getItemPosition ( @NonNull Object object ) {

            return mPagerAdapter.getItemPosition( object );
      }

      @Override
      public void registerDataSetObserver ( @NonNull DataSetObserver observer ) {

            mPagerAdapter.registerDataSetObserver( observer );
      }

      @Override
      public void unregisterDataSetObserver ( @NonNull DataSetObserver observer ) {

            mPagerAdapter.unregisterDataSetObserver( observer );
      }

      @Override
      public void notifyDataSetChanged ( ) {

            mPagerAdapter.notifyDataSetChanged();
      }
}

