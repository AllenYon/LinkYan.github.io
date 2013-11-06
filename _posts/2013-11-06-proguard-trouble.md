---
layout: post
title:  Android混淆问题伤透脑筋
---

不止一次为Android的混淆问题而头痛了.第三方包,注解编程,Gson的使用,都需要修改混淆配置文件,为此需要做个梳理

```
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

###################     RoboGuice   ##################################
#-dontwarn android.support.**
#-dontwarn roboguice.**
#-keep class com.google.inject.** { *; }
#-keep class com.google.inject.** { *; }
#-keep class javax.inject.** { *; }
#-keep class javax.annotation.** { *; }
#-keep class roboguice.** { *; }


#-keepclassmembers class * {
#    void *(**On*Event);
#}

#-keepattributes Signature

#-keepclassmembers class * {
#    @com.google.inject.Inject <fields>;
#    @com.google.inject.Inject <init>(...);
#}

###########################################################

-dontwarn com.weibo.sdk.**
-dontwarn android.net.http.**
-keep class com.weibo.sdk.** { *; }
-keep class android.net.http.** { *; }


# Tencent
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage { *;}

-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-dontwarn com.tencent.**
-keep class com.tencent.** { *; }



-dontwarn com.huaban.lib.ptr.**
-keep class com.huaban.lib.ptr.** { *; }

-dontwarn com.squareup.**
-keep class com.squareup.** { *; }

-dontwarn com.huaban.lib.photoview.**
-keep class com.huaban.lib.photoview.** {*;}

-keepclassmembers class * {
    public <methods>;
}


# UMeng
-keep public class com.umeng.fb.ui.ThreadView {
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-dontwarn com.umeng.**

-keepattributes *Annotation*

-keep class com.umeng*.** {*; }

#-keep public class com.huaban.R$*{
#    public static final int *;
#}


-dontwarn org.apache.commons.**
-keep class org.apache.commons.** {*;}

# GSON
-keepattributes Signature
# Gson specific classes
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.huaban.lib.api.model.** { *; }


###########################################
-dontwarn com.huaban.lib.annotation.**
-keep class com.huaban.lib.annotation.** { *; }
-keepnames class * { @com.huaban.lib.annotation.ViewInject *;}
-keepattributes Signature
-keepclassmembers class * {
    @com.huaban.lib.annotation.ViewInject <fields>;
    @com.huaban.lib.annotation.Viewinject <init>(...);
}
###########################################

#-dontwarn
#-ignorewarnings

```


















