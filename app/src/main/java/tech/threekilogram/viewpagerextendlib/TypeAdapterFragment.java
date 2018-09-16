package tech.threekilogram.viewpagerextendlib;

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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import tech.threekilogram.viewpager.adapter.BaseTypePagerAdapter;

/**
 * @author Liujin 2018-09-16:8:37
 */
public class TypeAdapterFragment extends Fragment {

      private ViewPager mViewPager;

      public static TypeAdapterFragment newInstance ( ) {

            return new TypeAdapterFragment();
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
      }

      private class FragmentAdapter extends BaseTypePagerAdapter {

            private int[] mBackGround = {
                getResources().getColor( R.color.orange ),
                getResources().getColor( R.color.red ),
                getResources().getColor( R.color.green ),
                getResources().getColor( R.color.purple ),
                getResources().getColor( R.color.black )
            };

            private int[] mPictures = {
                R.drawable.c130,
                R.drawable.c274,
                R.drawable.c465
            };

            @Override
            public int getCount ( ) {

                  return 8;
            }

            @Override
            protected int getViewType ( int position ) {

                  if( position < 5 ) {
                        return 0;
                  } else {
                        return 1;
                  }
            }

            @Override
            protected Object getData ( int position, int type ) {

                  if( position < 5 ) {
                        return "item " + position;
                  }

                  return null;
            }

            @Override
            protected View getView ( ViewGroup container, int position, int type ) {

                  if( position < 5 ) {
                        TextView textView = new TextView( container.getContext() );
                        textView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 48 );
                        textView.setGravity( Gravity.CENTER );
                        textView.setTextColor( Color.WHITE );

                        return textView;
                  } else {
                        ImageView imageView = new ImageView( container.getContext() );
                        imageView.setScaleType( ScaleType.CENTER_INSIDE );

                        return imageView;
                  }
            }

            @Override
            protected void bindData ( int position, Object data, View view, int type ) {

                  if( position < 5 ) {

                        ( (TextView) view ).setText( (String) data );
                        view.setBackgroundColor( mBackGround[ position ] );
                  } else {

                        ( (ImageView) view ).setImageResource( mPictures[ position - 5 ] );
                  }
            }
      }
}
