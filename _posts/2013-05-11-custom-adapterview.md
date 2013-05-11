---
layout: post
title: 创建属于你自己的3D ListView (译文)
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



