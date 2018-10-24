package tech.threekilogram.viewpagerextendlib;

import android.content.Context;
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
import tech.threekilogram.pager.image.ScaleImageViewPager;
import tech.threekilogram.pager.image.ScaleImageViewPager.SimpleImageAdapter;

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

            mPager.setAdapter( new PagerAdapter( getContext() ) );
      }

      private static class PagerAdapter extends SimpleImageAdapter {

            private Context mContext;

            public PagerAdapter ( Context context ) {

                  mContext = context;
            }

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
            protected Bitmap getBitmapAt ( int position ) {

                  return BitmapFactory.decodeResource( mContext.getResources(), res[ position ] );
            }
      }
}
