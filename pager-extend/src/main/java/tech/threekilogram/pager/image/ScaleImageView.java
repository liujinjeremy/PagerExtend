package tech.threekilogram.pager.image;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

/**
 * 支持手势缩放的ImageView,底层通过{@link Canvas}操作
 *
 * @author Liujin 2018-09-15:10:54
 */
public class ScaleImageView extends android.support.v7.widget.AppCompatImageView {

      /**
       * x方向缩放系数
       */
      protected float mCanvasScaleX    = 1;
      protected float mMinCanvasScaleX = 0;
      /**
       * y方向缩放系数
       */
      protected float mCanvasScaleY    = 1;
      protected float mMinCanvasScaleY = 0;
      /**
       * 缩放中心位置x
       */
      protected float mPivotPointX     = 0.5f;
      /**
       * 缩放中心位置y
       */
      protected float mPivotPointY     = 0.5f;
      /**
       * x方向移动距离
       */
      protected float mTranslateX;
      /**
       * y方向移动距离
       */
      protected float mTranslateY;

      /**
       * 控制缩放手势是否启用,true启用
       */
      protected boolean isScaleGestureEnabled     = true;
      /**
       * 控制移动手势是否启用,true启用
       */
      protected boolean isTranslateGestureEnabled = true;
      /**
       * 是否启用触摸事件
       */
      protected boolean isTouchEventEnabled       = false;

      /**
       * 缩放手势支持
       */
      protected ScaleGestureDetector mScaleGestureDetector;
      /**
       * 移动手势支持
       */
      protected GestureDetector      mTranslateGestureDetector;

      /**
       * 保存drawable显示区域坐标
       */
      protected RectF         mDrawableRect;
      /**
       * 保存canvas显示区域坐标
       */
      protected Rect          mCanvasRect;
      private   ValueAnimator mAnimator;

      public ScaleImageView ( Context context ) {

            this( context, null, 0 );
      }

      public ScaleImageView (
          Context context, @Nullable AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public ScaleImageView (
          Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            initField( context );
      }

      /**
       * 初始化变量
       */
      private void initField ( Context context ) {

            mScaleGestureDetector = new ScaleGestureDetector(
                context,
                new ScaleGestureListener()
            );

            mTranslateGestureDetector = new GestureDetector(
                context,
                new GestureListener()
            );

            mDrawableRect = new RectF();
            mCanvasRect = new Rect();
      }

      @Override
      protected void onDraw ( Canvas canvas ) {

            /* 获取drawable显示区域 */
            canvas.getClipBounds( mCanvasRect );
            getDrawableRectByScaleType( mCanvasRect );
            /* 计算显示图片区域相对canvas的偏移量 */
            float dLeftOffset = mDrawableRect.left - mCanvasRect.left;
            float dTopOffset = mDrawableRect.top - mCanvasRect.top;
            float dRightOffset = mDrawableRect.right - mCanvasRect.right;
            float dBottomOffset = mDrawableRect.bottom - mCanvasRect.bottom;

            /* 1.计算缩放中心 */
            float centerX = mCanvasRect.width() * mPivotPointX;
            float centerY = mCanvasRect.height() * mPivotPointY;
            /* 2.获取canvas显示区域 */
            float canvasLeft = centerX - centerX * mCanvasScaleX + mTranslateX;
            float canvasTop = centerY - centerY * mCanvasScaleY + mTranslateY;
            float canvasRight =
                centerX + ( mCanvasRect.width() - centerX ) * mCanvasScaleX + mTranslateX;
            float canvasBottom =
                centerY + ( mCanvasRect.height() - centerY ) * mCanvasScaleY + mTranslateY;
            /* 3.根据偏移量从canvas区域获取图片显示区域 */
            float dLeft = canvasLeft + dLeftOffset * mCanvasScaleX;
            float dTop = canvasTop + dTopOffset * mCanvasScaleY;
            float dRight = canvasRight + dRightOffset * mCanvasScaleX;
            float dBottom = canvasBottom + dBottomOffset * mCanvasScaleY;
            /* 修正显示区域 */
            mDrawableRect.set( dLeft, dTop, dRight, dBottom );

            /* 缩放 */
            canvas.scale(
                mCanvasScaleX,
                mCanvasScaleY,
                canvas.getWidth() * mPivotPointX,
                canvas.getHeight() * mPivotPointY
            );
            /* 位移 */
            float dx = mTranslateX / mCanvasScaleX;
            float dy = mTranslateY / mCanvasScaleY;
            canvas.translate( dx, dy );
            /* 绘制 */
            super.onDraw( canvas );
      }

      /**
       * 获取当前drawable显示区域坐标,由
       * {@link #getScaleType()},
       * {@link #mPivotPointX}{@link #mPivotPointY},
       * {@link #mCanvasScaleX}{@link #mCanvasScaleY},
       * {@link #mTranslateX}{@link #mTranslateY},
       * 共同决定
       */
      public RectF getDrawableRect ( ) {

            return mDrawableRect;
      }

      /**
       * 获取当前drawable显示区域坐标,由
       * {@link #getScaleType()},
       * {@link #mPivotPointX}{@link #mPivotPointY},
       * {@link #mCanvasScaleX}{@link #mCanvasScaleY},
       * {@link #mTranslateX}{@link #mTranslateY},
       * {@link Drawable#getIntrinsicWidth()}{Drawable#getIntrinsicHeight()}
       * 共同决定
       *
       * @param rectF 保存坐标的容器
       */
      public void getDrawableRect ( RectF rectF ) {

            rectF.set( mDrawableRect );
      }

      private void getDrawableRectByScaleType ( Rect canvasRect ) {

            if( getDrawable() == null ) {
                  mDrawableRect.set( canvasRect );
                  return;
            }

            if( getScaleType() == ScaleType.CENTER ) {

                  getCenterRect( canvasRect );
                  return;
            }

            if( getScaleType() == ScaleType.CENTER_CROP ) {

                  getCenterCropRect( canvasRect );
                  return;
            }

            if( getScaleType() == ScaleType.CENTER_INSIDE ) {

                  int width = getDrawable().getIntrinsicWidth();
                  int height = getDrawable().getIntrinsicHeight();

                  if( width < canvasRect.width() && height < canvasRect.height() ) {
                        getCenterRect( canvasRect );
                  } else {
                        getCenterInsideRect( canvasRect );
                  }
                  return;
            }

            if( getScaleType() == ScaleType.FIT_START ) {

                  getFitStartRect( canvasRect );
                  return;
            }

            if( getScaleType() == ScaleType.FIT_CENTER ) {
                  getCenterInsideRect( canvasRect );
                  return;
            }

            if( getScaleType() == ScaleType.FIT_END ) {
                  getFitEndRect( canvasRect );
                  return;
            }

            /* 如果是matrix或者fitXY,那么设置为显示区域 */
            mDrawableRect.set( canvasRect );
      }

      private void getFitEndRect ( Rect canvasRect ) {

            int width = getDrawable().getIntrinsicWidth();
            int height = getDrawable().getIntrinsicHeight();

            float fateWidth = canvasRect.width() / width * 1f;
            float fateHeight = canvasRect.height() / height * 1f;

            if( fateWidth > fateHeight ) {

                  float cropWidth = canvasRect.height() * 1f * width / height;

                  float xOffset = ( cropWidth - canvasRect.width() );

                  mDrawableRect.set(
                      mCanvasRect.left - xOffset,
                      mCanvasRect.top,
                      mCanvasRect.right,
                      mCanvasRect.bottom
                  );
            } else {

                  float cropHeight = canvasRect.width() * 1f * height / width;

                  float yOffset = ( cropHeight - canvasRect.height() );

                  mDrawableRect.set(
                      mCanvasRect.left,
                      mCanvasRect.top - yOffset,
                      mCanvasRect.right,
                      mCanvasRect.bottom
                  );
            }
      }

      private void getFitStartRect ( Rect canvasRect ) {

            int width = getDrawable().getIntrinsicWidth();
            int height = getDrawable().getIntrinsicHeight();

            float fateWidth = canvasRect.width() / width * 1f;
            float fateHeight = canvasRect.height() / height * 1f;

            if( fateWidth > fateHeight ) {

                  float cropWidth = canvasRect.height() * 1f * width / height;

                  float xOffset = ( cropWidth - canvasRect.width() );

                  mDrawableRect.set(
                      mCanvasRect.left,
                      mCanvasRect.top,
                      mCanvasRect.right + xOffset,
                      mCanvasRect.bottom
                  );
            } else {

                  float cropHeight = canvasRect.width() * 1f * height / width;

                  float yOffset = ( cropHeight - canvasRect.height() );

                  mDrawableRect.set(
                      mCanvasRect.left,
                      mCanvasRect.top,
                      mCanvasRect.right,
                      mCanvasRect.bottom + yOffset
                  );
            }
      }

      private void getCenterInsideRect ( Rect canvasRect ) {

            int width = getDrawable().getIntrinsicWidth();
            int height = getDrawable().getIntrinsicHeight();

            float fateWidth = canvasRect.width() / width * 1f;
            float fateHeight = canvasRect.height() / height * 1f;

            if( fateWidth > fateHeight ) {

                  float cropWidth = canvasRect.height() * 1f * width / height;

                  float xOffset = ( cropWidth - canvasRect.width() ) / 2;

                  mDrawableRect.set(
                      mCanvasRect.left - xOffset,
                      mCanvasRect.top,
                      mCanvasRect.right + xOffset,
                      mCanvasRect.bottom
                  );
            } else {

                  float cropHeight = canvasRect.width() * 1f * height / width;

                  float yOffset = ( cropHeight - canvasRect.height() ) / 2;

                  mDrawableRect.set(
                      mCanvasRect.left,
                      mCanvasRect.top - yOffset,
                      mCanvasRect.right,
                      mCanvasRect.bottom + yOffset
                  );
            }
      }

      private void getCenterCropRect ( Rect canvasRect ) {

            int width = getDrawable().getIntrinsicWidth();
            int height = getDrawable().getIntrinsicHeight();

            float fateWidth = canvasRect.width() / width * 1f;
            float fateHeight = canvasRect.height() / height * 1f;

            if( fateWidth <= fateHeight ) {

                  float cropWidth = canvasRect.height() * 1f * width / height;

                  float xOffset = ( cropWidth - canvasRect.width() ) / 2;

                  mDrawableRect.set(
                      mCanvasRect.left - xOffset,
                      mCanvasRect.top,
                      mCanvasRect.right + xOffset,
                      mCanvasRect.bottom
                  );
            } else {

                  float cropHeight = canvasRect.width() * 1f * height / width;

                  float yOffset = ( cropHeight - canvasRect.height() ) / 2;

                  mDrawableRect.set(
                      mCanvasRect.left,
                      mCanvasRect.top - yOffset,
                      mCanvasRect.right,
                      mCanvasRect.bottom + yOffset
                  );
            }
      }

      private void getCenterRect ( Rect canvasRect ) {

            int width = getDrawable().getIntrinsicWidth();
            int height = getDrawable().getIntrinsicHeight();

            float xOffset = ( width - canvasRect.width() ) >> 1;
            float yOffset = ( height - canvasRect.height() ) >> 1;

            mDrawableRect.set(
                canvasRect.left - xOffset,
                canvasRect.top - yOffset,
                canvasRect.right + xOffset,
                canvasRect.bottom + yOffset
            );
      }

      /**
       * true: 手势缩放已经启用
       */
      public boolean isScaleGestureEnabled ( ) {

            return isScaleGestureEnabled;
      }

      /**
       * 设置手势缩放是否启用
       *
       * @param scaleGestureEnabled true 启用
       */
      public void setScaleGestureEnabled ( boolean scaleGestureEnabled ) {

            isScaleGestureEnabled = scaleGestureEnabled;
      }

      /**
       * true: 手势移动已经启用
       */
      public boolean isTranslateGestureEnabled ( ) {

            return isTranslateGestureEnabled;
      }

      /**
       * 设置手势移动是否启用
       *
       * @param translateGestureEnabled true 启用
       */
      public void setTranslateGestureEnabled ( boolean translateGestureEnabled ) {

            isTranslateGestureEnabled = translateGestureEnabled;
      }

      /**
       * 是否启用touch事件
       *
       * @param touchEventEnabled true:启动
       */
      public void setTouchEventEnabled ( boolean touchEventEnabled ) {

            this.isTouchEventEnabled = touchEventEnabled;
      }

      /**
       * 处理手势
       */
      @SuppressLint("ClickableViewAccessibility")
      @Override
      public boolean onTouchEvent ( MotionEvent event ) {

            if( isTouchEventEnabled ) {

                  handleScaleGesture( event );
                  handleTranslateGesture( event );
                  super.onTouchEvent( event );
                  return true;
            } else {
                  return super.onTouchEvent( event );
            }
      }

      /**
       * 用于处理touch事件
       */
      public void handleScaleGesture ( MotionEvent event ) {

            /* 处理缩放 */
            if( isScaleGestureEnabled ) {
                  mScaleGestureDetector.onTouchEvent( event );
            }
      }

      /**
       * 用于处理touch事件
       */
      public void handleTranslateGesture ( MotionEvent event ) {

            /* 处理移动 */
            if( isTranslateGestureEnabled ) {
                  mTranslateGestureDetector.onTouchEvent( event );
            }
      }

      public void setMinCanvasScaleX ( float minCanvasScaleX ) {

            mMinCanvasScaleX = minCanvasScaleX;
      }

      /**
       * 在x方向缩放
       *
       * @param scaleX 缩放大小
       */
      public void setCanvasScaleX ( float scaleX ) {

            setCanvasScaleX( scaleX, mPivotPointX, mPivotPointY );
      }

      /**
       * 以指定位置为缩放中心,在x方向缩放
       *
       * @param scaleX 缩放大小
       * @param pivotX 缩放中心x百分比位置,0代表已左边为约束,1代表以右边为约束,0~1之间代表按比例位于左边和右边之间的一个位置
       * @param pivotY 缩放中心y百分比位置,0代表已上边为约束,1代表以低边为约束,0~1之间代表按比例位于上边和低边之间的一个位置
       */
      public void setCanvasScaleX (
          float scaleX,
          @FloatRange(from = 0, to = 1) float pivotX,
          @FloatRange(from = 0, to = 1) float pivotY ) {

            if( scaleX < mMinCanvasScaleX ) {
                  scaleX = mMinCanvasScaleX;
            }

            if( mCanvasScaleX == scaleX ) {
                  return;
            }

            mCanvasScaleX = scaleX;

            if( pivotX < 0 ) {
                  pivotX = 0;
            } else if( pivotX > 1 ) {
                  pivotX = 1;
            }
            if( pivotY < 0 ) {
                  pivotY = 0;
            } else if( pivotY > 1 ) {
                  pivotY = 1;
            }
            mPivotPointX = pivotX;
            mPivotPointY = pivotY;

            invalidate();
      }

      /**
       * 获取设置的缩放大小
       */
      public float getCanvasScaleX ( ) {

            return mCanvasScaleX;
      }

      public void setMinCanvasScaleY ( float minCanvasScaleY ) {

            mMinCanvasScaleY = minCanvasScaleY;
      }

      /**
       * 在y方向缩放
       *
       * @param scaleY 缩放大小
       */
      public void setCanvasScaleY ( float scaleY ) {

            setCanvasScaleY( scaleY, mPivotPointX, mPivotPointY );
      }

      /**
       * 以指定位置为缩放中心,在y方向缩放
       *
       * @param scaleY 缩放大小
       * @param pivotX 缩放中心x百分比位置,0代表已左边为约束,1代表以右边为约束,0~1之间代表按比例位于左边和右边之间的一个位置
       * @param pivotY 缩放中心y百分比位置,0代表已上边为约束,1代表以低边为约束,0~1之间代表按比例位于上边和低边之间的一个位置
       */
      public void setCanvasScaleY (
          float scaleY,
          @FloatRange(from = 0, to = 1) float pivotX,
          @FloatRange(from = 0, to = 1) float pivotY ) {

            if( scaleY < mMinCanvasScaleY ) {
                  scaleY = mMinCanvasScaleY;
            }

            if( mCanvasScaleY == scaleY ) {
                  return;
            }

            mCanvasScaleY = scaleY;

            if( pivotX < 0 ) {
                  pivotX = 0;
            } else if( pivotX > 1 ) {
                  pivotX = 1;
            }
            if( pivotY < 0 ) {
                  pivotY = 0;
            } else if( pivotY > 1 ) {
                  pivotY = 1;
            }
            mPivotPointX = pivotX;
            mPivotPointY = pivotY;

            invalidate();
      }

      /**
       * 获取设置的缩放大小
       */
      public float getCanvasScaleY ( ) {

            return mCanvasScaleY;
      }

      /**
       * 在x和y方向缩放
       */
      public void setCanvasScale ( float scaleX, float scaleY ) {

            setCanvasScale( scaleX, scaleY, mPivotPointX, mPivotPointY );
      }

      /**
       * 以指定位置为缩放中心,在x和y方向缩放
       *
       * @param pivotX 缩放中心x百分比位置,0代表已左边为约束,1代表以右边为约束,0~1之间代表按比例位于左边和右边之间的一个位置
       * @param pivotY 缩放中心y百分比位置,0代表已上边为约束,1代表以低边为约束,0~1之间代表按比例位于上边和低边之间的一个位置
       */
      public void setCanvasScale ( float scaleX, float scaleY, float pivotX, float pivotY ) {

            if( scaleY < mMinCanvasScaleY ) {
                  scaleY = mMinCanvasScaleY;
            }

            if( scaleX < mMinCanvasScaleX ) {
                  scaleX = mMinCanvasScaleX;
            }

            if( pivotX < 0 ) {
                  pivotX = 0;
            } else if( pivotX > 1 ) {
                  pivotX = 1;
            }
            if( pivotY < 0 ) {
                  pivotY = 0;
            } else if( pivotY > 1 ) {
                  pivotY = 1;
            }

            if( scaleX == mCanvasScaleX && scaleY == mCanvasScaleY && pivotX == mPivotPointX
                && pivotY == mPivotPointY ) {
                  return;
            }

            mCanvasScaleX = scaleX;
            mCanvasScaleY = scaleY;
            mPivotPointX = pivotX;
            mPivotPointY = pivotY;

            invalidate();
      }

      /**
       * 设置X方向距离左边移动距离
       *
       * @param translateX 移动距离
       */
      public void setTranslateX ( float translateX ) {

            if( mTranslateX == translateX ) {
                  return;
            }

            mTranslateX = translateX;
            invalidate();
      }

      /**
       * 获取设置的移动距离
       */
      public float getTranslateX ( ) {

            return mTranslateX;
      }

      /**
       * 设置Y方向距离左边移动距离
       *
       * @param translateY 移动距离
       */
      public void setTranslateY ( float translateY ) {

            if( mTranslateY == translateY ) {
                  return;
            }
            mTranslateY = translateY;
            invalidate();
      }

      /**
       * 获取设置的移动距离
       */
      public float getTranslateY ( ) {

            return mTranslateY;
      }

      public void setTranslate ( float translateX, float translateY ) {

            boolean call = false;
            if( mTranslateX != translateX ) {
                  mTranslateX = translateX;
                  call = true;
            }

            if( mTranslateY != translateY ) {
                  mTranslateY = translateY;
                  call = true;
            }

            if( call ) {
                  invalidate();
            }
      }

      public void setChange (
          float scaleX, float scaleY,
          float translateX, float translateY ) {

            setChange( scaleX, scaleY, mPivotPointX, mPivotPointY, translateX, translateY );
      }

      public void setChange (
          float scaleX, float scaleY,
          float pivotX, float pivotY,
          float translateX, float translateY ) {

            setCanvasScale( scaleX, scaleY, pivotX, pivotY );
            setTranslate( translateX, translateY );
      }

      public void reset ( ) {

            setChange( 1, 1, 0.5f, 0.5f, 0, 0 );
      }

      /**
       * 手势缩放支持
       */
      private class ScaleGestureListener implements OnScaleGestureListener {

            @Override
            public boolean onScale ( ScaleGestureDetector detector ) {

                  float factor = detector.getScaleFactor();
                  float v = mCanvasScaleX * factor;
                  float v1 = mCanvasScaleY * factor;

                  setCanvasScale( v, v1 );

                  return true;
            }

            @Override
            public boolean onScaleBegin ( ScaleGestureDetector detector ) {

                  return true;
            }

            @Override
            public void onScaleEnd ( ScaleGestureDetector detector ) {

            }
      }

      /**
       * 手势移动支持
       */
      private class GestureListener implements OnGestureListener {

            /**
             * 保存一共移动多少距离
             */
            private float mX;
            private float mY;

            @Override
            public boolean onDown ( MotionEvent e ) {

                  return true;
            }

            @Override
            public void onShowPress ( MotionEvent e ) {

            }

            @Override
            public boolean onSingleTapUp ( MotionEvent e ) {

                  return false;
            }

            @Override
            public boolean onScroll (
                MotionEvent e1, MotionEvent e2, float distanceX, float distanceY ) {

                  mX += distanceX;
                  mY += distanceY;
                  setTranslate( -mX, -mY );
                  return true;
            }

            @Override
            public void onLongPress ( MotionEvent e ) {

            }

            @Override
            public boolean onFling (
                MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) {

                  return false;
            }
      }
}