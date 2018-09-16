package tech.threekilogram.viewpagerextendlib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.threekilogram.viewpager.adapter.BasePagerAdapter;
import tech.threekilogram.viewpager.observer.OnPagerScrollObserver;
import tech.threekilogram.viewpager.observer.PagerScroll;

/**
 * @author Liujin 2018-09-16:8:37
 */
public class ScrollObserverFragment extends Fragment {

      private static final String TAG = ScrollObserverFragment.class.getSimpleName();

      private ViewPager mViewPager;

      public static ScrollObserverFragment newInstance ( ) {

            return new ScrollObserverFragment();
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater,
          @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_maxcount, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mViewPager = itemView.findViewById( R.id.banner );
            mViewPager.setAdapter( new FragmentAdapter() );
            PagerScroll pagerScroll = new PagerScroll( mViewPager );
            pagerScroll.setOnPagerScrollObserver( new OnPagerScrollObserver() {

                  @Override
                  public void onCurrent ( int currentPosition, float offset ) {

                        String format = String.format( "%.4f", offset );
                        Log.e( TAG, "onCurrent : " + currentPosition + " " + format );
                  }

                  @Override
                  public void onNext ( int nextPosition, float offset ) {

                        String format = String.format( "%.4f", offset );
                        Log.e( TAG, "onNext : " + nextPosition + " " + format );
                  }

                  @Override
                  public void onPageSelected ( int position ) {

                        Log.e( TAG, "onPageSelected : " + position );
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