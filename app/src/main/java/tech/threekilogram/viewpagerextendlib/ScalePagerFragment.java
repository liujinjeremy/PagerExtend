package tech.threekilogram.viewpagerextendlib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import tech.threekilogram.pager.adapter.BasePagerAdapter;
import tech.threekilogram.pager.image.ScaleImageView;
import tech.threekilogram.pager.image.ScaleImageViewPager;

/**
 * @author Liujin 2018-10-23:15:22
 */
public class ScalePagerFragment extends Fragment {

      private ScaleImageViewPager mPager;
      private FrameLayout         mRoot;

      public static ScalePagerFragment newInstance ( ) {

            return new ScalePagerFragment();
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_scale_pager, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mPager = itemView.findViewById( R.id.pager );
            mRoot = itemView.findViewById( R.id.root );

            mPager.setAdapter( new PagerAdapter() );
      }

      private class PagerAdapter extends BasePagerAdapter {

            private int[] res = {
                R.drawable.a292,
                R.drawable.a2559,
                R.drawable.a207,
                R.drawable.a188
            };

            @Override
            public int getCount ( ) {

                  return res.length;
            }

            @Override
            protected Object getData ( int position ) {

                  return BitmapFactory.decodeResource( getResources(), res[ position ] );
            }

            @Override
            protected View getView ( ViewGroup container, int position ) {

                  return new ScaleImageView( container.getContext() );
            }

            @Override
            protected void bindData ( int position, Object data, View view ) {

                  ( (ScaleImageView) view ).setImageBitmap( (Bitmap) data );
            }
      }
}
