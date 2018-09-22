package tech.threekilogram.viewpagerextendlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tech.threekilogram.pager.indicator.DotView;

/**
 * @author Liujin 2018-09-16:17:31
 */
public class DotViewFragment extends Fragment {

      private DotView mDotView;

      public static DotViewFragment newInstance ( ) {

            Bundle args = new Bundle();

            DotViewFragment fragment = new DotViewFragment();
            fragment.setArguments( args );
            return fragment;
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_dot, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mDotView = itemView.findViewById( R.id.dotView );
            mDotView.setDotCount( 5 );
            mDotView.setDotMargin( 50 );
            mDotView.setDotSize( 50 );
            mDotView.setSelected( 1 );
      }
}
