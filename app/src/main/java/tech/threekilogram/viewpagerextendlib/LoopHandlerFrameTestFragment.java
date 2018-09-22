package tech.threekilogram.viewpagerextendlib;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import tech.threekilogram.pager.banner.LoopHandlerLayout;
import tech.threekilogram.pager.banner.LoopHandlerLayout.OnLoopListener;

/**
 * @author Liujin 2018-09-21:23:23
 */
public class LoopHandlerFrameTestFragment extends Fragment {

      private static final String TAG = LoopHandlerFrameTestFragment.class.getSimpleName();
      private LoopHandlerLayout mLoopFrame;

      public static LoopHandlerFrameTestFragment newInstance ( ) {

            Bundle args = new Bundle();

            LoopHandlerFrameTestFragment fragment = new LoopHandlerFrameTestFragment();
            fragment.setArguments( args );
            return fragment;
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater,
          @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_loop_handler, container, false );
      }

      @Override
      public void onViewCreated (
          @NonNull View view,
          @Nullable Bundle savedInstanceState ) {

            initView( view );
      }

      @SuppressLint("ClickableViewAccessibility")
      private void initView ( @NonNull final View itemView ) {

            mLoopFrame = itemView.findViewById( R.id.LoopFrame );
            mLoopFrame.setOnLoopListener( new OnLoopListener() {

                  private int mCount = 0;

                  @Override
                  public void onLoop ( LoopHandlerLayout layout ) {

                        Log.e( TAG, "onLoop : " + ++mCount );
                  }
            } );
            mLoopFrame.startLoop();
            mLoopFrame.setOnTouchListener( new OnTouchListener() {

                  @Override
                  public boolean onTouch ( View v, MotionEvent event ) {

                        mLoopFrame.onTouchPauseLoop( event );
                        return true;
                  }
            } );
      }
}
