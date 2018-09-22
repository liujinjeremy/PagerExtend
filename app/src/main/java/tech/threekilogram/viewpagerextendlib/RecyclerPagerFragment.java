package tech.threekilogram.viewpagerextendlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.threekilogram.pager.pager.RecyclerPager;

/**
 * @author Liujin 2018-09-19:16:15
 */
public class RecyclerPagerFragment extends Fragment {

      private RecyclerPager mRecyclerPager;

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
            mRecyclerPager.setAdapter( new RecyclerAdapter() );
      }

      private class RecyclerAdapter extends RecyclerView.Adapter {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
                  View view = inflater.inflate( R.layout.item_recycler_pager, parent, false );
                  return new Holder( view );
            }

            @Override
            public void onBindViewHolder ( @NonNull ViewHolder holder, int position ) {

                  ( (Holder) holder ).bind( position );
            }

            @Override
            public int getItemCount ( ) {

                  return 20;
            }
      }

      public class Holder extends RecyclerView.ViewHolder {

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
