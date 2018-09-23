package tech.threekilogram.viewpagerextendlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.threekilogram.pager.scroll.recycler.OnRecyclerPagerScrollListener;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScrollListener;

/**
 * @author Liujin 2018-09-23:10:08
 */
public class RecyclerScrollTestFragment extends Fragment {

      private static final String TAG = RecyclerScrollTestFragment.class.getSimpleName();
      private RecyclerView mRecycler;

      public static RecyclerScrollTestFragment newInstance ( ) {

            Bundle args = new Bundle();

            RecyclerScrollTestFragment fragment = new RecyclerScrollTestFragment();
            fragment.setArguments( args );
            return fragment;
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_recycler_scroll, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mRecycler = itemView.findViewById( R.id.recycler );

            mRecycler = itemView.findViewById( R.id.recycler );
            LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
            layoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
            mRecycler.setLayoutManager( layoutManager );
            mRecycler.setAdapter( new MainAdapter() );
            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView( mRecycler );

            mRecycler.post( new Runnable() {

                  @Override
                  public void run ( ) {

                        int width = mRecycler.getWidth();
                        Log.e( TAG, "run : " + width );
                  }
            } );
            RecyclerPagerScrollListener listener = new RecyclerPagerScrollListener( mRecycler );
            listener.setOnRecyclerPagerScrollListener( new OnRecyclerPagerScrollListener() {

                  @Override
                  public void onScroll (
                      int state, int currentPosition, int nextPosition, int offsetX, int offsetY ) {

                        Log.e(
                            TAG,
                            "onScroll : " + RecyclerPagerScrollListener.scrollStateString( state )
                                + " " + currentPosition + " " + nextPosition + " " + offsetX
                                + " " + offsetY
                        );
                  }

                  @Override
                  public void onPageSelected ( int prevSelected, int newSelected ) {

                        Log.e( TAG, "onPageSelected : " + prevSelected + " " + newSelected );
                  }
            } );
            mRecycler.addOnScrollListener( listener );
      }

      private class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

            private int[] mColors = {
                R.color.blue,
                R.color.orange,
                R.color.red,
                R.color.green,
                R.color.purple
            };

            @NonNull
            @Override
            public MainViewHolder onCreateViewHolder (
                @NonNull ViewGroup parent, int viewType ) {

                  View view = LayoutInflater.from( getContext() )
                                            .inflate( R.layout.item_text, parent, false );
                  return new MainViewHolder( view );
            }

            @Override
            public void onBindViewHolder (
                @NonNull MainViewHolder holder, int position ) {

                  holder.bind( position, mColors[ position ] );
            }

            @Override
            public int getItemCount ( ) {

                  return mColors.length;
            }
      }

      private class MainViewHolder extends ViewHolder {

            MainViewHolder ( View itemView ) {

                  super( itemView );
            }

            void bind ( int position, int res ) {

                  ( (TextView) itemView ).setText( "Item : " + position );
                  itemView.setBackgroundResource( res );
            }
      }
}
