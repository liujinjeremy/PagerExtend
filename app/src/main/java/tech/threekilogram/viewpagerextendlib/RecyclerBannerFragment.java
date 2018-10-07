package tech.threekilogram.viewpagerextendlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.threekilogram.pager.banner.RecyclerPagerBanner;
import tech.threekilogram.pager.banner.RecyclerPagerBanner.BannerAdapter;
import tech.threekilogram.pager.scroll.recycler.OnRecyclerPagerScrollListener;
import tech.threekilogram.pager.scroll.recycler.RecyclerPagerScroll;

/**
 * @author Liujin 2018-09-22:12:32
 */
public class RecyclerBannerFragment extends Fragment {

      private static final String TAG = RecyclerBannerFragment.class.getSimpleName();

      private RecyclerPagerBanner mRecyclerBanner;

      public static RecyclerBannerFragment newInstance ( ) {

            Bundle args = new Bundle();

            RecyclerBannerFragment fragment = new RecyclerBannerFragment();
            fragment.setArguments( args );
            return fragment;
      }

      @Nullable
      @Override
      public View onCreateView (
          @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState ) {

            return inflater.inflate( R.layout.fragment_recycler_banner, container, false );
      }

      @Override
      public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {

            super.onViewCreated( view, savedInstanceState );
            initView( view );
      }

      private void initView ( @NonNull final View itemView ) {

            mRecyclerBanner = itemView.findViewById( R.id.recyclerBanner );
            final RecyclerAdapter adapter = new RecyclerAdapter();
            mRecyclerBanner.setBannerAdapter( adapter );
            mRecyclerBanner.post( new Runnable() {

                  @Override
                  public void run ( ) {

                        mRecyclerBanner.startLoop();
                  }
            } );
            mRecyclerBanner.addOnScrollListener( new OnScrollListener() {

                  @Override
                  public void onScrollStateChanged ( RecyclerView recyclerView, int newState ) {

                        super.onScrollStateChanged( recyclerView, newState );
                  }

                  @Override
                  public void onScrolled ( RecyclerView recyclerView, int dx, int dy ) {

                        super.onScrolled( recyclerView, dx, dy );
                  }
            } );
            RecyclerPagerScroll scroll = new RecyclerPagerScroll(
                mRecyclerBanner.getRecyclerPager() );
            scroll.setOnRecyclerPagerScrollListener( new OnRecyclerPagerScrollListener() {

                  @Override
                  public void onScroll (
                      int state, int currentPosition, int nextPosition, int offsetX, int offsetY ) {

                        Log.e( TAG, "onScroll : " + adapter.getActualPosition( currentPosition ) );
                  }

                  @Override
                  public void onPageSelected ( int prevSelected, int newSelected ) {

                  }
            } );
      }

      private class RecyclerAdapter extends BannerAdapter<Holder> {

            @Override
            public int getActualCount ( ) {

                  return 5;
            }

            @NonNull
            @Override
            public Holder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
                  View view = inflater.inflate( R.layout.item_recycler_pager, parent, false );
                  return new Holder( view );
            }

            @Override
            public void onBindViewHolder (
                @NonNull Holder holder, int position ) {

                  holder.bind( getActualPosition( position ) );
            }
      }

      private class Holder extends RecyclerView.ViewHolder {

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
