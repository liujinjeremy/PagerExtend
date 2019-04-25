package tech.liujin.viewpagerextendlib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.liujin.pager.adapter.BasePagerAdapter;
import tech.liujin.pager.scroll.pager.ViewPagerScrollListener;
import tech.liujin.viewpagerextendlib.widget.IndicatorView;

/**
 * @author Liujin 2018-09-16:8:37
 */
public class PagerScrollFragment extends Fragment {

      private static final String TAG = PagerScrollFragment.class.getSimpleName();

      private ViewPager     mViewPager;
      private IndicatorView mIndicator;

      public static PagerScrollFragment newInstance ( ) {

            return new PagerScrollFragment();
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater,
          @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_scroller, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mViewPager = itemView.findViewById( R.id.banner );
            FragmentAdapter adapter = new FragmentAdapter();
            mViewPager.setAdapter( adapter );

            mIndicator = itemView.findViewById( R.id.indicator );
            mIndicator.setCount( adapter.getCount() );

            mViewPager.addOnPageChangeListener( new ViewPagerScrollListener( mViewPager ) {

                  @Override
                  protected void onScrolled (
                      int state, int current, float offset, int offsetPixels ) {

                        mIndicator.setXOff( current, -offset );
                  }
            } );
      }

      private class FragmentAdapter extends BasePagerAdapter<String, TextView> {

            private int[] mBackGround = {
                getResources().getColor( R.color.orange ),
                getResources().getColor( R.color.red ),
                getResources().getColor( R.color.green ),
                getResources().getColor( R.color.purple ),
                getResources().getColor( R.color.black )
            };

            @Override
            public int getCount ( ) {

                  return 5;
            }

            @Override
            protected String getData ( int position ) {

                  return "item " + position;
            }

            @Override
            protected TextView getView ( ViewGroup container, int position ) {

                  TextView textView = new TextView( container.getContext() );
                  textView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 48 );
                  textView.setGravity( Gravity.CENTER );
                  textView.setTextColor( Color.WHITE );

                  return textView;
            }

            @Override
            protected void bindData ( int position, String data, TextView view ) {

                  view.setText( data );
                  view.setBackgroundColor( mBackGround[ position ] );
            }
      }
}
