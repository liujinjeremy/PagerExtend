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
import tech.threekilogram.pager.image.ImageWatcherPager;
import tech.threekilogram.pager.image.ImageWatcherPager.ImageWatcherAdapter;

/**
 * @author Liujin 2018-10-05:11:42
 */
public class ImageWatcherFragment extends Fragment {

      private ImageWatcherPager mImageWatcher;

      public static ImageWatcherFragment newInstance ( ) {

            ImageWatcherFragment fragment = new ImageWatcherFragment();
            return fragment;
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_image_watcher, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mImageWatcher = itemView.findViewById( R.id.imageWatcher );
            mImageWatcher.setImageWatcherAdapter( new ImageAdapter() );
      }

      private class ImageAdapter extends ImageWatcherAdapter {

            private int[] res = {
                R.drawable.c130,
                R.drawable.c274,
                R.drawable.c465
            };

            @Override
            protected int getImageCount ( ) {

                  return res.length;
            }

            @Override
            protected Bitmap getImage ( int position ) {

                  return BitmapFactory.decodeResource( getResources(), res[ position ] );
            }
      }
}
