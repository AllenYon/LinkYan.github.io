---
layout: post
title:  Android--FingerPaint (Graphics Demos)
---

前言：源码取自Android SDK的APIDemos项目的Graphics章节,FingerPaint小Demo。在本示例用户可以在屏幕上随意涂鸦线条或者图形，并且有多种参数可以选择

首先创建一个继承View的类

```
public class MyView extends View { 
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;   // 1

        public MyView(Context context) {
            super(context);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);   //2
        }
}
```

-  @1 TOUCH_TOLERANCE常量用来判断手指滑动的最小距离
-  @2 Paint.DITHER_FLAG  官方解释是 bit mask for the flag enabling dithering,允许抖动的标志位

接着重写onTouchEvent(MotionEvent event)函数

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);              
				invalidate();				    
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
		}
		return true;
	}

	private void touch_start(float x, float y) {
		mPath.reset();							// 1
		mPath.moveTo(x, y);						// 2
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {    // 7  
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);    // 3 
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);                              // 4
		mCanvas.drawPath(mPath, mPaint);		            // 5
		mPath.reset();							            // 6
	}
	
- 画线的主要步骤就是 1->2->3->4->6 
- @3 mPath.quadTo(float x1,float y1,float x2,float y2) 的官方注释是 `Add a quadratic bezier from the last point, approaching control point (x1,y1), and ending at (x2,y2). If no moveTo() call has been made for this contour, the first point is automatically set to (0,0).` 从最后一个触摸点到前一个触摸点连接一条二次贝塞尔曲线，如果没有调用moveTo()的话，默认第一个点为(0,0).
- @5 这条函数调用作用是将Path绘制在Bitmap上

由于我们调用了invalidate();函数 所以我们也必须重写onDraw(Canvas canvas)将我们所画的路径真正的绘制到屏幕上。

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFAAAAAA);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);    // 1
		canvas.drawPath(mPath, mPaint);			            // 2
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh); 
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);  //3
		mCanvas = new Canvas(mBitmap);                                 //4
	}

- @3 为了将我们画的路径能够保存下来，我们就需要创建一个Bitmap对象来维持。mCanvas实例在touch_up()调用时才将路径绘制到Bitmap上，并且在 @1 时调用canvas.drawBitmap()将Bitmap绘制到屏幕上。
- @2 使用canvas.drawPath()函数绘制路径。

至此我们的View就创建完了，接着需要一个Activity来显示这个View。

	private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(new MyView(this)); 
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);                        // 1   
        mPaint.setStrokeJoin(Paint.Join.ROUND);                     // 2
        mPaint.setStrokeCap(Paint.Cap.ROUND);                       // 3
        mPaint.setStrokeWidth(12); 
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);  // 4
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);            // 5 
    }
    
- @1 Paint.Style 有三种枚举类型，FILL，STROKE，FILL_AND_STROKE。前面两种都还好理解，填充和线条，第三种会同时绘制线条和填充，可以自己修改下参数体会体会。
- @2 @3 官方注释最完整,

```
	 /**
     * The Cap specifies the treatment for the beginning and ending of
     * stroked lines and paths. The default is BUTT.
     */
    public enum Cap {
        /**
         * The stroke ends with the path, and does not project beyond it.
         */
        BUTT    (0),
        /**
         * The stroke projects out as a semicircle, with the center at the
         * end of the path.
         */
        ROUND   (1),
        /**
         * The stroke projects out as a square, with the center at the end
         * of the path.
         */
        SQUARE  (2);
        
        private Cap(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }

    /**
     * The Join specifies the treatment where lines and curve segments
     * join on a stroked path. The default is MITER.
     */
    public enum Join {
        /**
         * The outer edges of a join meet at a sharp angle
         */
        MITER   (0),
        /**
         * The outer edges of a join meet in a circular arc.
         */
        ROUND   (1),
        /**
         * The outer edges of a join meet with a straight line
         */
        BEVEL   (2);
        
        private Join(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }
```

- @4 浮雕效果

```
	 /**
     * Create an emboss maskfilter
     *
     * @param direction  array of 3 scalars [x, y, z] specifying the direction of the light source
     * @param ambient    0...1 amount of ambient light
     * @param specular   coefficient for specular highlights (e.g. 8)
     * @param blurRadius amount to blur before applying lighting (e.g. 3)
     * @return           the emboss maskfilter
     */
    public EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius) {
    
    }
```
- @5 模糊效果

```
	 /**
     * Create a blur maskfilter.
     *
     * @param radius The radius to extend the blur from the original mask. Must be > 0.
     * @param style  The Blur to use
     * @return       The new blur maskfilter
     */
    public BlurMaskFilter(float radius, Blur style) {
        native_instance = nativeConstructor(radius, style.native_int);
    }
```



 

