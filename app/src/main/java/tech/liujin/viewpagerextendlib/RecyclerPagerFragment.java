package tech.liujin.viewpagerextendlib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import tech.liujin.pager.pager.RecyclerPager;

/**
 * @author Liujin 2018-09-19:16:15
 */
public class RecyclerPagerFragment extends Fragment implements OnClickListener {

      private static final String TAG = RecyclerPagerFragment.class.getSimpleName();

      private RecyclerPager mRecyclerPager;
      private RecyclerPager mRecyclerPagerV;
      private Button        mGetPosition;

      public static RecyclerPagerFragment newInstance ( ) {

            Bundle args = new Bundle();

            RecyclerPagerFragment fragment = new RecyclerPagerFragment();
            fragment.setArguments( args );
            return fragment;
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_recycler_pager, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mRecyclerPager = itemView.findViewById( R.id.recyclerPager );
            mRecyclerPagerV = itemView.findViewById( R.id.recyclerPagerV );

            mRecyclerPager.setAdapter( new RecyclerAdapter() );

            mRecyclerPagerV.setOrientation( RecyclerView.VERTICAL );
            mRecyclerPagerV.setAdapter( new RecyclerAdapter() );

            mRecyclerPager.scrollToPosition( 4 );
            mRecyclerPager.post( new Runnable() {

                  @Override
                  public void run ( ) {

                        mRecyclerPager.smoothScrollToPosition( 0 );
                  }
            } );

            Log.e( TAG, "initView : " + mRecyclerPager.getCurrentPosition() );
            mGetPosition = itemView.findViewById( R.id.getPosition );
            mGetPosition.setOnClickListener( this );

            mRecyclerPager.addOnScrollListener( new OnScrollListener() {

                  private int mPosition = mRecyclerPager.getCurrentPosition();

                  @Override
                  public void onScrollStateChanged ( RecyclerView recyclerView, int newState ) { }

                  @Override
                  public void onScrolled ( RecyclerView recyclerView, int dx, int dy ) {

                        super.onScrolled( recyclerView, dx, dy );
                        int currentPosition = mRecyclerPager.getCurrentPosition();
                        if( currentPosition != mPosition ) {
                              mPosition = currentPosition;
                              Log.e( TAG, "onScrolled : new mPosition " + mPosition );
                        }
                  }
            } );
      }

      @Override
      public void onClick ( View v ) {

            switch( v.getId() ) {
                  case R.id.getPosition:
                        Log.e( TAG, "initView : " + mRecyclerPager.getCurrentPosition() );
                        break;
                  default:
                        break;
            }
      }

      private class RecyclerAdapter extends Adapter {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  TextView textView = new TextView( getContext() );
                  textView.setTextColor( Color.WHITE );
                  textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 24 );
                  textView.setGravity( Gravity.CENTER );
                  return new Holder( textView );
            }

            @Override
            public void onBindViewHolder ( @NonNull ViewHolder holder, int position ) {

                  ( (Holder) holder ).bind( position );
            }

            @Override
            public int getItemCount ( ) {

                  return 5;
            }
      }

      public class Holder extends ViewHolder {

            private int[] colors = {
                R.color.black,
                R.color.orange,
                R.color.purple,
                R.color.green,
                R.color.red
            };

            public Holder ( View itemView ) {

                  super( itemView );
            }

            void bind ( int position ) {

                  ( (TextView) itemView ).setText( "Item : " + position );
                  int i = position % colors.length;
                  itemView.setBackgroundColor( getResources().getColor( colors[ i ] ) );
            }
      }
}
