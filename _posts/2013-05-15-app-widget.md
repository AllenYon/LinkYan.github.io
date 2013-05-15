---
layout: post
title: Android开发者指南之App Widgets (译)
---
App Widgets是一种能嵌入到其他应用程序(如Home screen)的小型应用程序界面，并且能接收定时的更新。这些视图被称为用户界面中的窗口小部件，需要App Widget provider才能发布.能持有其他App Widgets的应用组件叫做App Widget host.下面显示了一张Music App Widget的图。

![music app widget](http://developer.android.com/images/appwidgets/appwidget.png)

本文档将描述如何使用App Widget provider去发布一个App Widget。

> ####Widget Design
> 更多关于如何设计你的app widget,你可以阅读这篇 [窗口小部件设计指导](http://developer.android.com/design/patterns/widgets.html). 

##基础
创建一个App Widget ，你需要以下条件:

1. AppWidgetProviderInfo 对象。用来描述App Widget的元数据，比如App Widget的布局，更新频率，AppWidgetProvider类。也可以使用xml定义。
2. AppWidgetProvider 类实例。定义了一些基本方法，需要你去实现这些接口，是基于广播(broadcast)事件的。通过它，你可以接收到什么时候App Widget被更新了，可使用，不可使用和被删除了等状态。
3. 界面布局。定义了为App Widget初始化的布局，定义在XML中。

此外，你可以实现一个App Widget配置Activity。这个可选的Activity的主要作用是，当用户在启动器(launches)中添加了你的App Widget时允许他在创建的时候就修改App Widget的设置。

以下各节描述了如何设置每个组件

##在Manifest中声明一个App Widget
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

这个```<receiver>```元素需要android:name属性，用来指定App Widget所使用的AppWidgetProvider。

这个```<intent-filter>```元素必须包含一个带有android:name属性的```<action>```元素。这个属性指定了AppWidgetProvider接收ACTION_APPWIDGET_UPDATE广播。这是你必须要显示声明的广播。AppWidgetManager会自动发生其他所有的App Widget广播到AppWidgetProvider中，如果有必要的话。

这个```<meta-data>```元素指定了




















