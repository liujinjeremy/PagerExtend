package tech.threekilogram.viewpager.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Liujin 2018-09-19:16:05
 */
public class RecyclerPager extends RecyclerView {

      public RecyclerPager ( Context context ) {

            this( context, null, 0 );
      }

      public RecyclerPager (
          Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public RecyclerPager ( Context context, @Nullable AttributeSet attrs, int defStyle ) {

            super( context, attrs, defStyle );
            init();
      }

      private void init ( ) {

            LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
            layoutManager.setOrientation( HORIZONTAL );
            setLayoutManager( layoutManager );

            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView( this );
      }

      public void setOrientation ( @RecyclerView.Orientation int orientation ) {

            ( (LinearLayoutManager) getLayoutManager() ).setOrientation( orientation );
      }
}
