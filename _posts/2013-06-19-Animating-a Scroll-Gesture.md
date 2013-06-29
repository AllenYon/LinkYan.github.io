---
layout: post
title:  Android Training-Animating a Scroll Gesture
---
 
In Android, scrolling is typically achieved by using the ScrollView class. Any standard layout that might extend beyond the bounds of its container should be nested in a ScrollView to provide a scrollable view that's managed by the framework. Implementing a custom scroller should only be necessary for special scenarios. This lesson describes such a scenario: displaying a scrolling effect in response to touch gestures using scrollers.

在Android中，通常我们使用ScrollView类来实现滑动。任何标准的布局都可能超出容器本身的边界，需要嵌套在由框架管理的可滑动视图的ScrollView中去展示。只有在需要特定的场景中，我们才可能需要去实现一个自定的Scroller。本章节描述了这么一个场景，显示一个使用了滑动器，响应触摸手势的滑动效果。

You can use scrollers (Scroller or OverScroller) to collect the data you need to produce a scrolling animation in response to a touch event. They are similar, but OverScroller includes methods for indicating to users that they've reached the content edges after a pan or fling gesture. The InteractiveChart sample uses the EdgeEffect class (actually the EdgeEffectCompat class) to display a "glow" effect when users reach the content edges.

你可以使用Scroller（如 Scroller 或者 OverScroller）去收集生产一个滑动动画所需要响应的触摸事件的数据。它们很类似，但是OverScroller包含指示用户甩动手势已经到达内容边界的方法。InteractiveChart示例使用了EdgeEffect类(准确地说是 EdgeEffectCompat类)去显示发光的效果，当用户到达内容的边界。


Note: We recommend that you use OverScroller rather than Scroller for scrolling animations. OverScroller provides the best backward compatibility with older devices. 

注意：我们建议你使用OverScroller而不是Scroller去实现滑动动画。OverScroller为旧的设备提供了最好的向后兼容性。

Also note that you generally only need to use scrollers when implementing scrolling yourself. ScrollView and HorizontalScrollView do all of this for you if you nest your layout within them.

同样提醒你通常只有你要自己实现滑动时才使用scrollers。ScrollView和HorizontalScrollView已经为你准备好一切，如果你把布局嵌套其中。

A scroller is used to animate scrolling over time, using platform-standard scrolling physics (friction, velocity, etc.). The scroller itself doesn't actually draw anything. Scrollers track scroll offsets for you over time, but they don't automatically apply those positions to your view. It's your responsibility to get and apply new coordinates at a rate that will make the scrolling animation look smooth.

Scroller被用于随时间推移动画滚动，使用平台标准的滚动物理（摩擦，速度等）。Scroller其实本身不绘制任何东西。Scrollers随时间推移跟踪滚动偏移，但是他们不会自动地将你的视图移动到那些位置。



Understand Scrolling Terminology
"Scrolling" is a word that can take on different meanings in Android, depending on the context.

Scrolling is the general process of moving the viewport (that is, the 'window' of content you're looking at). When scrolling is in both the x and y axes, it's called panning. The sample application provided with this class, InteractiveChart, illustrates two different types of scrolling, dragging and flinging:

Dragging is the type of scrolling that occurs when a user drags her finger across the touch screen. Simple dragging is often implemented by overriding onScroll() in GestureDetector.OnGestureListener. For more discussion of dragging, see Dragging and Scaling.
Flinging is the type of scrolling that occurs when a user drags and lifts her finger quickly. After the user lifts her finger, you generally want to keep scrolling (moving the viewport), but decelerate until the viewport stops moving. Flinging can be implemented by overriding onFling() in GestureDetector.OnGestureListener, and by using a scroller object. This is the use case that is the topic of this lesson.
It's common to use scroller objects in conjunction with a fling gesture, but they can be used in pretty much any context where you want the UI to display scrolling in response to a touch event. For example, you could override onTouchEvent() to process touch events directly, and produce a scrolling effect or a "snapping to page" animation in response to those touch events.

Implement Touch-Based Scrolling
This section describes how to use a scroller. The snippet shown below comes from the InteractiveChart sample provided with this class. It uses a GestureDetector, and overrides the GestureDetector.SimpleOnGestureListener method onFling(). It uses OverScroller to track the fling gesture. If the user reaches the content edges after the fling gesture, the app displays a "glow" effect.

Note: The InteractiveChart sample app displays a chart that you can zoom, pan, scroll, and so on. In the following snippet, mContentRect represents the rectangle coordinates within the view that the chart will be drawn into. At any given time, a subset of the total chart domain and range are drawn into this rectangular area. mCurrentViewport represents the portion of the chart that is currently visible in the screen. Because pixel offsets are generally treated as integers, mContentRect is of the type Rect. Because the graph domain and range are decimal/float values, mCurrentViewport is of the type RectF.

The first part of the snippet shows the implementation of onFling():

// The current viewport. This rectangle represents the currently visible 
// chart domain and range. The viewport is the part of the app that the
// user manipulates via touch gestures.
private RectF mCurrentViewport = 
        new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);

// The current destination rectangle (in pixel coordinates) into which the 
// chart data should be drawn.
private Rect mContentRect;

private OverScroller mScroller;
private RectF mScrollerStartViewport;
...
private final GestureDetector.SimpleOnGestureListener mGestureListener
        = new GestureDetector.SimpleOnGestureListener() {
    @Override
    public boolean onDown(MotionEvent e) {
        // Initiates the decay phase of any active edge effects.
        releaseEdgeEffects();
        mScrollerStartViewport.set(mCurrentViewport);
        // Aborts any active scroll animations and invalidates.
        mScroller.forceFinished(true);
        ViewCompat.postInvalidateOnAnimation(InteractiveLineGraphView.this);
        return true;
    }
    ...
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, 
            float velocityX, float velocityY) {
        fling((int) -velocityX, (int) -velocityY);
        return true;
    }
};

private void fling(int velocityX, int velocityY) {
    // Initiates the decay phase of any active edge effects.
    releaseEdgeEffects();
    // Flings use math in pixels (as opposed to math based on the viewport).
    Point surfaceSize = computeScrollSurfaceSize();
    mScrollerStartViewport.set(mCurrentViewport);
    int startX = (int) (surfaceSize.x * (mScrollerStartViewport.left - 
            AXIS_X_MIN) / (
            AXIS_X_MAX - AXIS_X_MIN));
    int startY = (int) (surfaceSize.y * (AXIS_Y_MAX - 
            mScrollerStartViewport.bottom) / (
            AXIS_Y_MAX - AXIS_Y_MIN));
    // Before flinging, aborts the current animation.
    mScroller.forceFinished(true);
    // Begins the animation
    mScroller.fling(
            // Current scroll position
            startX,
            startY,
            velocityX,
            velocityY,
            /*
             * Minimum and maximum scroll positions. The minimum scroll 
             * position is generally zero and the maximum scroll position 
             * is generally the content size less the screen size. So if the 
             * content width is 1000 pixels and the screen width is 200  
             * pixels, the maximum scroll offset should be 800 pixels.
             */
            0, surfaceSize.x - mContentRect.width(),
            0, surfaceSize.y - mContentRect.height(),
            // The edges of the content. This comes into play when using
            // the EdgeEffect class to draw "glow" overlays.
            mContentRect.width() / 2,
            mContentRect.height() / 2);
    // Invalidates to trigger computeScroll()
    ViewCompat.postInvalidateOnAnimation(this);
}
When onFling() calls postInvalidateOnAnimation(), it triggers computeScroll() to update the values for x and y. This is typically be done when a view child is animating a scroll using a scroller object, as in this example.

Most views pass the scroller object's x and y position directly to scrollTo(). The following implementation of computeScroll() takes a different approach—it calls computeScrollOffset() to get the current location of x and y. When the criteria for displaying an overscroll "glow" edge effect are met (the display is zoomed in, x or y is out of bounds, and the app isn't already showing an overscroll), the code sets up the overscroll glow effect and calls postInvalidateOnAnimation() to trigger an invalidate on the view:

// Edge effect / overscroll tracking objects.
private EdgeEffectCompat mEdgeEffectTop;
private EdgeEffectCompat mEdgeEffectBottom;
private EdgeEffectCompat mEdgeEffectLeft;
private EdgeEffectCompat mEdgeEffectRight;

private boolean mEdgeEffectTopActive;
private boolean mEdgeEffectBottomActive;
private boolean mEdgeEffectLeftActive;
private boolean mEdgeEffectRightActive;

@Override
public void computeScroll() {
    super.computeScroll();

    boolean needsInvalidate = false;

    // The scroller isn't finished, meaning a fling or programmatic pan 
    // operation is currently active.
    if (mScroller.computeScrollOffset()) {
        Point surfaceSize = computeScrollSurfaceSize();
        int currX = mScroller.getCurrX();
        int currY = mScroller.getCurrY();

        boolean canScrollX = (mCurrentViewport.left > AXIS_X_MIN
                || mCurrentViewport.right < AXIS_X_MAX);
        boolean canScrollY = (mCurrentViewport.top > AXIS_Y_MIN
                || mCurrentViewport.bottom < AXIS_Y_MAX);

        /*          
         * If you are zoomed in and currX or currY is
         * outside of bounds and you're not already
         * showing overscroll, then render the overscroll
         * glow edge effect.
         */
        if (canScrollX
                && currX < 0
                && mEdgeEffectLeft.isFinished()
                && !mEdgeEffectLeftActive) {
            mEdgeEffectLeft.onAbsorb((int) 
                    OverScrollerCompat.getCurrVelocity(mScroller));
            mEdgeEffectLeftActive = true;
            needsInvalidate = true;
        } else if (canScrollX
                && currX > (surfaceSize.x - mContentRect.width())
                && mEdgeEffectRight.isFinished()
                && !mEdgeEffectRightActive) {
            mEdgeEffectRight.onAbsorb((int) 
                    OverScrollerCompat.getCurrVelocity(mScroller));
            mEdgeEffectRightActive = true;
            needsInvalidate = true;
        }

        if (canScrollY
                && currY < 0
                && mEdgeEffectTop.isFinished()
                && !mEdgeEffectTopActive) {
            mEdgeEffectTop.onAbsorb((int) 
                    OverScrollerCompat.getCurrVelocity(mScroller));
            mEdgeEffectTopActive = true;
            needsInvalidate = true;
        } else if (canScrollY
                && currY > (surfaceSize.y - mContentRect.height())
                && mEdgeEffectBottom.isFinished()
                && !mEdgeEffectBottomActive) {
            mEdgeEffectBottom.onAbsorb((int) 
                    OverScrollerCompat.getCurrVelocity(mScroller));
            mEdgeEffectBottomActive = true;
            needsInvalidate = true;
        }
        ...
    }
Here is the section of the code that performs the actual zoom:

// Custom object that is functionally similar to Scroller
Zoomer mZoomer;
private PointF mZoomFocalPoint = new PointF();
...

// If a zoom is in progress (either programmatically or via double
// touch), performs the zoom.
if (mZoomer.computeZoom()) {
    float newWidth = (1f - mZoomer.getCurrZoom()) * 
            mScrollerStartViewport.width();
    float newHeight = (1f - mZoomer.getCurrZoom()) * 
            mScrollerStartViewport.height();
    float pointWithinViewportX = (mZoomFocalPoint.x - 
            mScrollerStartViewport.left)
            / mScrollerStartViewport.width();
    float pointWithinViewportY = (mZoomFocalPoint.y - 
            mScrollerStartViewport.top)
            / mScrollerStartViewport.height();
    mCurrentViewport.set(
            mZoomFocalPoint.x - newWidth * pointWithinViewportX,
            mZoomFocalPoint.y - newHeight * pointWithinViewportY,
            mZoomFocalPoint.x + newWidth * (1 - pointWithinViewportX),
            mZoomFocalPoint.y + newHeight * (1 - pointWithinViewportY));
    constrainViewport();
    needsInvalidate = true;
}
if (needsInvalidate) {
    ViewCompat.postInvalidateOnAnimation(this);
}
This is the computeScrollSurfaceSize() method that's called in the above snippet. It computes the current scrollable surface size, in pixels. For example, if the entire chart area is visible, this is simply the current size of mContentRect. If the chart is zoomed in 200% in both directions, the returned size will be twice as large horizontally and vertically.

private Point computeScrollSurfaceSize() {
    return new Point(
            (int) (mContentRect.width() * (AXIS_X_MAX - AXIS_X_MIN)
                    / mCurrentViewport.width()),
            (int) (mContentRect.height() * (AXIS_Y_MAX - AXIS_Y_MIN)
                    / mCurrentViewport.height()));
}
For another example of scroller usage, see the source code for the ViewPager class. It scrolls in response to flings, and uses scrolling to implement the "snapping to page" animation.


 

