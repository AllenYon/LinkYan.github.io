---
layout: post
title: 创建属于你自己的3D ListView (译)
---

本文是教你如何在安卓应用中做出很酷效果的ListView的教材第一章，我叫Anders Ericson，主要从事UI工作，在Timescape应用中有个就是我做的，你可以在Xperia X10 mini 和 Xperia X10 mini pro中找到.当用户尝试使用一个App时，UI是他们第一个关注的东西,所以我决定做一个教材，使得其他任何安卓开发者可以创建属于他们自己的ListView,类似与在Timescape中的3D感觉和物理特性(dynamics).

在教材的第一章，我们将创建一个很基础的list，然后在接下去的两章中，越来越多的功能和特性将添加进来。我同时也会教你如何使用list的基本构造，然后改变它使得无论如何工作的都使你的应用做到最好。下面那个链接是第一章的源码，已经为你准备好构建在如Eclipse这样的IDE中。同时不要忘了下载Sony Ericsson Tutorials 应用从Android Market中，你可以尝试使用同样的应用在教材的每一步。我很期待看到你的评论和问题。

Android中标准的ListView已经支持了很多东西，并且包括了绝大多数的你可以想到的用户事件。但是listview看起来太平淡了，当你想继承它并且做很多事情时，都发现做不到，然后无疾而终。标准ListView的另一个缺点是缺乏好的物理特性（和改变它的能力）。因此，如果你要你的UI看起来不那么普通的话，你要做的仅仅是实现你自己的View.

因为在一篇文章中写完会有太多的代码，所以我准备分为3部分。第一部分（就是这章）先创建一个基本的List，有太多的东西要去包括，但我想让我们更关注它们在后面几章.第二章，我们将看到listview的外貌改变和一些类3D图像算法。最后一章，我们将改变list的行为并且添加一些物理动力学进去，一些非常能提升外观和感觉的东西。

虽然这里用到的技术和在X10 Mini上用到的一样，当这篇教材的目的不是仅仅拷贝一个看起来很特殊的list，而是教你实现自己的listview。我很确定你肯定有很多关于你的listview该看起来怎么样，它的行为和它用来干嘛的主意。

### Hello AdapterView

当我们瞄准一个list（能显示其他view）,我们需要继承ViewGroup,更合适的是AdapterView。（原因，或者更多的原因我们不从AbsListView继承，是因为它不允许我们在list上做有活力的效果）。那么，让我们从开始建立安卓项目并且创建一个继承自AdapterView<Adapter> 的 MyListView 开始吧，AdapterView有四个抽象函数我们需要去实现:

- getAdapter()
- setAdapter()
- getSelectedView()
- setSelection()

getAdapter()和setAdapter()直接实现，其他两个现在先仅仅抛出异常.

```
public class MyListView extends AdapterView {
    /** The adapter with all the data */
    private Adapter mAdapter;
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        removeAllViewsInLayout();
        requestLayout();
    }
    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }
    @Override
    public void setSelection(int position) {
        throw new UnsupportedOperationException("Not supported");
    }
    @Override
    public View getSelectedView() {
        throw new UnsupportedOperationException("Not supported");
    }
}
```
这里唯一值得注意的是setAdapter方法。当我们获得一个新的Adapter我们移除之前所有的视图，然后请求一个布局，并且按照adapter放置视图。如果我们现在创建一个activity和带有假数据的adapter，然后使用新视图，我们将得不到任何东西，在屏幕上。这是因为如果我们要在屏幕上得到一些东西，我们需要重写onLayout()方法。

#### 显示我们的第一个视图

在[onLayout( )](http://developer.android.com/reference/android/view/View.html#onLayout(boolean,%20int,%20int,%20int,%20int) )中，我们从adpater中获取视图，并且添加他们作为一个子视图。

```
@Override
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    // if we don't have an adapter, we don't need to do anything
    if (mAdapter == null) {
        return;
    }
    if (getChildCount() == 0) {
        int position = 0;
        int bottomEdge = 0;
        while (bottomEdge &lt; getHeight() &amp;&amp; position &lt; mAdapter.getCount()) {
            View newBottomChild = mAdapter.getView(position, null, this);
            addAndMeasureChild(newBottomChild);
            bottomEdge += newBottomChild.getMeasuredHeight();
            position++;
        }
    }
    positionItems();
}
```
So，What happends hers? 首先执行父调用和空值检查，然后开始真正有用的代码。如果我们还没有添加任何子节点，那么我们就开始那么做。这个while循环直到我们添加足够多的视图覆盖整个屏幕为止。当我们从adapter获得一个视图，我们就把它添加为一个子节点，然后我们需要测量(measure)它，为的是得到它的正确尺寸。当我们添加完所有的视图，我们把它们放置到正确的位置.


```
/**
 * Adds a view as a child view and takes care of measuring it
 *
 * @param child The view to add
 */
private void addAndMeasureChild(View child) {
    LayoutParams params = child.getLayoutParams();
    if (params == null) {
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
    addViewInLayout(child, -1, params, true);
    int itemWidth = getWidth();
    child.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
}
/**
 * Positions the children at the &quot;correct&quot; positions
 */
private void positionItems() {
    int top = 0;
    for (int index = 0; index < getChildCount(); index++) {
        View child = getChildAt(index);
        int width = child.getMeasuredWidth();
        int height = child.getMeasuredHeight();
        int left = (getWidth() - width) / 2;
        child.layout(left, top, left + width, top + height);
        top += height;
    }
}

```
这些代码比较简单，自解释，所以我不准备过多描述。虽然我在测量子视图时走了点捷径，但是这些代码在大部分情况下是工作良好的。positioItems()从top（0)开始，然后布局这些子视图，一个挨着一个，没有任何padding。值得注意的是，我们忽略了list可能有的padding.

### 滑动
如果现在运行这些代码，我们在屏幕上得到一些东西了。然而，这一点交互的感觉也没有（interactive）。当我们触摸屏幕时它不会滑动，我们也不能点击任何item.要让触摸生效，我们需要重写 onTouchEvent()。

仅仅使其滑动的触摸逻辑是十分简单的，当我们得到一个按下事件，我们保存下按下事件的位置，和list的位置。我们将使用第一个item的top作为list的位置(position)，当我们获得一个移动事件，我们计算与按下事件的距离，然后根据开始位置和当前位置的距离重新安置list。

```
@Override
public boolean onTouchEvent(MotionEvent event) {
    if (getChildCount() == 0) {
        return false;
    }
    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchStartY = (int)event.getY();
            mListTopStart = getChildAt(0).getTop();
            break; 
        case MotionEvent.ACTION_MOVE:
            int scrolledDistance = (int)event.getY() - mTouchStartY;
            mListTop = mListTopStart + scrolledDistance;
            requestLayout();
            break;
        default:
            break;
    }
    return true;
}
```


































