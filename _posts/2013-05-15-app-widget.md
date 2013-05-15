---
layout: post
title: Android开发者指南之App Widgets (译)
---
App Widgets是一种能嵌入到其他应用程序(如Home screen)的小型应用程序界面，并且能接收定时的更新。这些视图被称为用户界面中的窗口小部件，需要App Widget provider才能发布.能持有其他App Widgets的应用组件叫做App Widget host.下面显示了一张Music App Widget的图。

![music app widget](http://developer.android.com/images/appwidgets/appwidget.png)

本文档将描述如何使用App Widget provider发布一个App Widget。

> ####Widget Design
> 更多关于如何设计你的app widget,你可以阅读这篇 [窗口小部件设计指导](http://developer.android.com/design/patterns/widgets.html). 

##基础
---
创建一个App Widget ，你需要以下条件:

1. AppWidgetProviderInfo 对象。用来描述App Widget的元数据，比如App Widget的布局，更新频率，AppWidgetProvider类。也可以使用xml定义。
2. AppWidgetProvider 类实例。定义了一些基本方法，需要你去实现这些接口，是基于广播(broadcast)事件的。通过它，你可以接收到什么时候App Widget被更新了，可使用，不可使用和被删除了等状态。
3. 界面布局。定义了为App Widget初始化的布局，定义在XML中。

此外，你可以实现一个App Widget配置Activity。这个可选的Activity的主要作用是，当用户在启动器(launches)中添加了你的App Widget时允许他在创建的时候就修改App Widget的设置。

以下各节描述了如何设置每个组件

##在Manifest中声明一个App Widget
---
首先，在你应用程序的AndroidManifest.xml文件中声明AppWidgetProvider类，像这样:

```
<receiver android:name="ExampleAppWidgetProvider" >
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data android:name="android.appwidget.provider"
               android:resource="@xml/example_appwidget_info" />
</receiver>
```

`<receiver>`元素需要android:name属性，用来指定App Widget所使用的AppWidgetProvider。

`<intent-filter>`元素必须包含一个带有android:name属性的`<action>`元素。这个属性指定了AppWidgetProvider接收`ACTION_APPWIDGET_UPDATE`广播。这是你必须要显示声明的广播。AppWidgetManager会自动发生其他所有的App Widget广播到AppWidgetProvider中，如果有必要的话。

`<meta-data>`元素指定了AppWidgetProviderInfo的来源并且要求有以下属性：
- android:name-指定元数据名称。使用`android.appwidget.provider`作为AppWidgetProviderInfo的描述来识别数据
- android:resource-指定AppWidgetProviderInfo的本地来源

##添加AppWidgetProviderInfo 元数据
---
AppWidgetProviderInfo定义了一个App Widget必要的特性（qualities），例如最小的布局尺寸，初始布局资源，多久更新一次App Widget，创建时调用的配置Activity。可以使用单个`<appwidget-provider>`元素的XML资源来定义AppWidgetProviderInfo对象，然后保持到项目的`res/xml/`文件夹下。

例如：

```
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="294dp"
    android:minHeight="72dp"
    android:updatePeriodMillis="86400000"
    android:previewImage="@drawable/preview"
    android:initialLayout="@layout/example_appwidget"
    android:configure="com.example.android.ExampleAppWidgetConfigure" 
    android:resizeMode="horizontal|vertical"
    android:widgetCategory="home_screen|keyguard"
    android:initialKeyguardLayout="@layout/example_keyguard">
</appwidget-provider>
```

关于`<appwidget-provider>`属性的一些总结:

- `miniWidth`和`minHeight`
- `minResizeWidth`和`minResizeHeight`
- `updatePeriodMillis`
- `initialLayout`
- `configure`
- `perviewImage`
- `autoAdvanceViewId`
- `resizeMode`
- `widgetCategory`
- `initialKeyguardLayout`

关于更多`<appwidget-provider>可以接收的属性的相关信息，可以查看[AppWidgetProviderInfo](http://developer.android.com/reference/android/appwidget/AppWidgetProviderInfo.html)类。

## 创建App Widget布局
---

你必须将你的App Widget初始化布局定义在XML中，并且将它保存在项目的`res/layout/`目录下。你可以使用下面列表列出的视图对象来设计你的App Widget,但是在你设计你的App Widget前，请先阅读并理解[App Widget Design Guidelines](http://developer.android.com/guide/practices/ui_guidelines/widget_design.html)。

创建一个App Widget布局是很简单的，如果你很了解[Layouts](http://developer.android.com/guide/topics/ui/declaring-layout.html)的话。但是你应该意识到App Widget是基于[RemoteViews](http://developer.android.com/reference/android/widget/RemoteViews.html)的，它不支持所有类型的布局和控件(view widget)。

一个RemoteViews对象(consequently 一个App Widget)可以支持以下的布局类:

- FrameLayout
- LinearLayout
- RelativeLayout
- GridLayout

和以下控件类:

- AnalogClock
- Button
- Chronometer
- ImageButton
- ImageView
- ProgressBar
- TextView
- ViewFlipper
- ListView
- GridView
- StackView
- AdapterViewFlipper

这些类的派生类(Descendants)是不支持的。

RemoteViews 也支持 ViewStub,一种不可见，不占大小的视图，你可以在运行时延迟`inflate`布局资源

### 为App Widget添加margins

## 使用AppWidgetProvider类
---
###接收App Widget广播意图 (broadcast intents)

## 创建一个App Widget配置Activity
---

###从配置Activity更新App Widget


## 设置预览图片
---

##使App Widget在锁屏上可用
---

###改变大小指导

##使用带有集合的App Widget
---
Android 3.0引进了带有集合的App Widget。这种App Widget使用RemoteViewService去显示那些返回远程数据的集合,例如从content provider。由RemoteViewsService提供的数据展示在App Widget中使用了以下视图类型中的一种,我们称之为"collections views"

- ListView:A view that shows items in a vertically scrolling list. For an example, see the Gmail app widget.
- GridView:A view that shows items in two-dimensional scrolling grid. For an example, see the Bookmarks app widget.
- StackView:A stacked card view (kind of like a rolodex), where the user can flick the front card up/down to see the previous/next card, respectively. Examples include the YouTube and Books app widgets. 
- AdapterViewFlipper:An adapter-backed simple ViewAnimator that animates between two or more views. Only one child is shown at a time.

正如上文所述,这些collections views显示


### 小样应用

### 实现带有集合的App Widget

### 带有集合的App Widget的清单


### 带有集合的App Widget的布局

### 带有集合的App Widget的AppWidgetProvider

### RemoteViewsService类

### RemoteViewsFactory接口

### 为个别Item添加行为

#### 设置 pendng intent template

#### 设置 fill-in intent

## 保持集合数据最新

下面的流程图发生在一个使用了集合的App Widget更新数据时。它展示了App Widget代码与RemoteViewsFactory的交互，以及你如何触发更新。

![fresh data](http://developer.android.com/images/appwidgets/appwidget_collections.png)

使用了集合的App Widgets的其中一个特性就是可以为用户提供最新的内容。例如，Android 3.0 Gmail app widget，可以为用户提供他们收件箱的一个快照。为了做到这一点，你需要能触发你的RemoteViewsFactory和集合view,去获得并且显示新数据。你可以通过使用AppWidgetManager调用notifyAppWidgetViewDataChanged()来实现这一点。然后在你的RemoteViewsFactory的OnDataChanged()获得一个回调结果，一个你有机会可以获取任何新数据的回调接口。注意，你可以在onDataSetChanged()回调方法中执行密集的同步操作(processing-intensive operations synchronously)。回调方法在元数据或者视图数据从RemoteViewsFactory那里获取之前以及被执行完成，所以你可以放心。此外，你也可以在getViewAt()方法内执行密集的同步操作。**如果这个回调函数执行了很长时间，那么加载视图(RemoteViewsFactory中指定的getLoadingView()方法)将被显示在集合视图中正确的位置，直到它返回结果.**












































