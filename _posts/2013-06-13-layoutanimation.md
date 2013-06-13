---
layout: post
title: Android-LayoutAnimation
---

###Class Overview
---
A layout animation controller is used to animated a layout's, or a view group's, children. Each child uses the same animation but for every one of them, the animation starts at a different time. A layout animation controller is used by ViewGroup to compute the delay by which each child's animation start must be offset. The delay is computed by using characteristics of each child, like its index in the view group. This standard implementation computes the delay by multiplying a fixed amount of miliseconds by the index of the child in its parent view group. Subclasses are supposed to override getDelayForView(android.view.View) to implement a different way of computing the delay. For instance, a GridLayoutAnimationController will compute the delay based on the column and row indices of the child in its parent view group. Information used to compute the animation delay of each child are stored in an instance of LayoutAnimationController.AnimationParameters, itself stored in the ViewGroup.LayoutParams of the view.

###XML Attributes
---
####android:animation

Animation to use on each child.

Must be a reference to another resource, in the form "@[+][package:]type:name" or to a theme attribute in the form "?[package:][type:]name".

This corresponds to the global attribute resource symbol animation.

The order in which the animations will be started.

Must be one of the following constant values.

<table>
<tr>
<td>Constant</td>
<td>Value</td>
<td>Description</td>
</tr>
<tr>
<td>normal</td>
<td>0</td>
<td>Animations are started in the natural order.</td>
</tr>
<tr>
<td>reverse</td>
<td>1</td>
<td>Animations are started in the reverse order.</td>
</tr>
<tr>
<td>random</td>
<td>2</td>
<td>Animations are started randomly.</td>
</tr>
</table>

This corresponds to the global attribute resource symbol animationOrder.

####android:delay

Fraction of the animation duration used to delay the beginning of the animation of each child.

May be a floating point value, such as "1.2".

May be a fractional value, which is a floating point number appended with either % or %p, such as "14.5%". The % suffix always means a percentage of the base size; the optional %p suffix provides a size relative to some parent container.

This may also be a reference to a resource (in the form "@[package:]type:name") or theme attribute (in the form "?[package:][type:]name") containing a value of this type.

This corresponds to the global attribute resource symbol delay.

Related Methods

####android:interpolator

Interpolator used to interpolate the delay between the start of each animation.

Must be a reference to another resource, in the form "@[+][package:]type:name" or to a theme attribute in the form "?[package:][type:]name".

This corresponds to the global attribute resource symbol interpolator.

layout/ac_listview.xml

```
<ListView 
	...
    android:layoutAnimation="@anim/layout_bottom_to_top_slide" />
```
 
anim/layout_bottom_to_top_slide.xml

```
<layoutAnimation xmlns:android="http://schemas.android.com/apk/res/android"
        android:delay="30%"
        android:animationOrder="reverse"
        android:animation="@anim/slide_right"
        android:interpolator="" />
```

anim/slide_right.xml

```
<set 
	xmlns:android="http://schemas.android.com/apk/res/android" 	android:interpolator="@android:anim/accelerate_interpolator">
    <translate android:fromXDelta="-100%p" android:toXDelta="0"
            android:duration="@android:integer/config_shortAnimTime" /> 
            <!-- config_shortAnimaTime=200 -->
</set>
```

