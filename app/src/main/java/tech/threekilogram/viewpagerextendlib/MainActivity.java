package tech.threekilogram.viewpagerextendlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

/**
 * @author liujin
 */
public class MainActivity extends AppCompatActivity {

      private FrameLayout    mContainer;
      private NavigationView mNavigationView;
      private DrawerLayout   mDrawer;

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_main );
            initView();
      }

      private void initView ( ) {

            mContainer = findViewById( R.id.container );
            mNavigationView = findViewById( R.id.navigationView );
            mNavigationView.setNavigationItemSelectedListener(
                new MainNavigationItemSelectedListener()
            );
            mDrawer = findViewById( R.id.drawer );
      }

      private void changeFragment ( Fragment fragment ) {

            getSupportFragmentManager().beginTransaction()
                                       .replace( R.id.container, fragment )
                                       .commit();
      }

      private class MainNavigationItemSelectedListener implements OnNavigationItemSelectedListener {

            @Override
            public boolean onNavigationItemSelected ( @NonNull MenuItem item ) {

                  switch( item.getItemId() ) {
                        case R.id.menu00:
                              changeFragment( MaxCountAdapterFragment.newInstance() );
                              break;
                        case R.id.menu01:
                              changeFragment( AdapterFragment.newInstance() );
                              break;
                        case R.id.menu02:
                              changeFragment( TypeAdapterFragment.newInstance() );
                              break;
                        case R.id.menu03:
                              changeFragment( ExtendPagerFragment.newInstance() );
                              break;
                        case R.id.menu05:
                              changeFragment( ScrollObserverFragment.newInstance() );
                              break;
                        case R.id.menu06:
                              changeFragment( DotViewFragment.newInstance() );
                              break;
                        case R.id.menu07:
                              changeFragment( RecyclerPagerFragment.newInstance() );
                              break;
                        case R.id.menu08:
                              changeFragment( LoopHandlerFrameTestFragment.newInstance() );
                              break;
                        case R.id.menu09:
                              changeFragment( ViewPagerBannerFragment.newInstance() );
                              break;
                        default:
                              break;
                  }

                  mDrawer.closeDrawer( Gravity.START );
                  return true;
            }
      }
}
