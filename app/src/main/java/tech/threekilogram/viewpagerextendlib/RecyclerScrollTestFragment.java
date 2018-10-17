package tech.threekilogram.viewpagerextendlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScrollListener;
import tech.threekilogram.viewpagerextendlib.widget.IndicatorView;

/**
 * @author Liujin 2018-09-23:10:08
 */
public class RecyclerScrollTestFragment extends Fragment {

      private static final String        TAG = RecyclerScrollTestFragment.class.getSimpleName();
      private              RecyclerView  mRecycler;
      private              IndicatorView mIndicator;

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
            mIndicator = itemView.findViewById( R.id.indicator );

            LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
            layoutManager.setOrientation( LinearLayoutManager.HORIZONTAL );
            mRecycler.setLayoutManager( layoutManager );

            MainAdapter adapter = new MainAdapter();
            mRecycler.setAdapter( adapter );

            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView( mRecycler );

            mIndicator.setCount( adapter.getItemCount() );

            mRecycler.addOnScrollListener( new RecyclerPagerScrollListener() {

                  @Override
                  protected void onScroll (
                      int state, int currentPosition, int nextPosition, int dx, int dy ) {

                        super.onScroll( state, currentPosition, nextPosition, dx, dy );
                        mIndicator.setXOff( currentPosition, dx * 1f / mRecycler.getWidth() );
                  }
            } );
      }

      private class MainAdapter extends Adapter<MainViewHolder> {

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
