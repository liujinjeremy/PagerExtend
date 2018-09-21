package tech.threekilogram.viewpager.adapter;

/**
 * @author Liujin 2018-09-21:22:49
 */
public class PositionAdapter {

      private int mMaxCount = Integer.MAX_VALUE;
      private int mActualCount;

      public PositionAdapter ( ) { }

      public PositionAdapter ( int maxCount ) {

            mMaxCount = maxCount;
      }

      public int getMaxCount ( ) {

            return mMaxCount;
      }

      public int getActualCount ( ) {

            return mActualCount;
      }

      public void setActualCount ( int actualCount ) {

            mActualCount = actualCount;
      }

      /**
       * @return {@link #getMaxCount()}的中间值,并且是{@link #getActualCount()}的倍数,使其正好是实际数据第一项
       */
      public int getStartPosition ( ) {

            int i = Integer.MAX_VALUE / 2;
            return i - ( i % mActualCount );
      }

      public int getActualPosition ( int position ) {

            return position % mActualCount;
      }

      public static void main ( String[] args ) {

            PositionAdapter adapter = new PositionAdapter();
            adapter.setActualCount( 5 );

            int startPosition = adapter.getStartPosition();
            System.out.println( startPosition );

            for( int i = startPosition; i < startPosition + 20; i++ ) {
                  int position = adapter.getActualPosition( i );
                  System.out.println( position );
            }
      }
}
