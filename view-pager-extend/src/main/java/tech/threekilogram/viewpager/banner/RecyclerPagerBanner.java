package tech.threekilogram.viewpager.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import tech.threekilogram.viewpager.pager.RecyclerPager;

/**
 * @author Liujin 2018-09-22:11:17
 */
public class RecyclerPagerBanner extends LoopHandlerLayout {

      protected RecyclerPager mRecyclerPager;

      public RecyclerPagerBanner ( @NonNull Context context ) {

            this( context, null, 0 );
      }

      public RecyclerPagerBanner (
          @NonNull Context context,
          @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public RecyclerPagerBanner (
          @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
      }

      @Override
      protected void onFinishInflate ( ) {

            super.onFinishInflate();
            mRecyclerPager = createPager( getContext() );
      }

      protected RecyclerPager createPager ( Context context ) {

            return new RecyclerPager( context );
      }
}
