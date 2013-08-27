---
layout: post
title:  Building and Running from the Command Line（译）
---

有两种方式构建你的App使用Ant构建脚本：一个是测试/调试你的App--调试更多，一个是构建你的最后包为了发布--发布更多。无论哪种方式构建你的App，它必须被签名在它安装到模拟器或者你的设备之前，使用调试密钥在调试模式下构建，使用你的私有密钥在发布模式下构建。

There are two ways to build your application using the Ant build script: one for testing/debugging your application — debug mode — and one for building your final package for release — release mode. Regardless of which way you build your application, it must be signed before it can install on an emulator or device—with a debug key when building in debug mode and with your own private key when building in release mode.

无论你是在调试模式或者发布模式下构建，你需要使用Ant工具去编译和构建你的项目。它会生成.apk文件一边你安装在模拟和或者设备。当你在调试模式下构建时，.apk文件自动的被SDK工具用调试密钥签名，所以它已经准备好被安装到模拟器或已连接着的开发机。你不能分发一个使用调试密钥签名的App。当你在发布模式下构建，.apk文件是未签名的，你必须手动使用你的私有密钥为它签名，使用Keytool和Jarsigner.

Whether you're building in debug mode or release mode, you need to use the Ant tool to compile and build your project. This will create the .apk file that you can install on an emulator or device. When you build in debug mode, the .apk file is automatically signed by the SDK tools with a debug key, so it's instantly ready for installation onto an emulator or attached development device. You cannot distribute an application that is signed with a debug key. When you build in release mode, the .apk file is unsigned, so you must manually sign it with your own private key, using Keytool and Jarsigner.

阅读并理解[Signing Your Applications](http://developer.android.com/tools/publishing/app-signing.html)很重要，特别是你准备发布你的App并且分享给终端用户。那篇文档描述了生成私有密钥的过程并且使用它为你的.apk文件签名。如果你仅仅刚刚开始，然后你可以快速的运行你的App在模拟器或者你的开发机上通过调试模式构建。

It's important that you read and understand Signing Your Applications, particularly once you're ready to release your application and share it with end-users. That document describes the procedure for generating a private key and then using it to sign your .apk file. If you're just getting started, however, you can quickly run your applications on an emulator or your own development device by building in debug mode.

如果你没有Ant，你可以从[Apache Ant Home page](http://ant.apache.org/)获取。安装并确保它在你的可执行PATH里。Before calling Ant,you need to declare the JAVA_HOME environment variable to sepecify the path to where the JDK is installed.

If you don't have Ant, you can obtain it from the Apache Ant home page. Install it and make sure it is in your executable PATH. Before calling Ant, you need to declare the JAVA_HOME environment variable to specify the path to where the JDK is installed.

Note: When installing JDK on Windows, the default is to install in the "Program Files" directory. This location will cause ant to fail, because of the space. To fix the problem, you can specify the JAVA_HOME variable like this:

	set JAVA_HOME=c:\Progra~1\Java\<jdkdir>
	
The easiest solution, however, is to install JDK in a non-space directory, for example:

	c:\java\jdk1.6.0_02
	
##调试模式中构建
---

对于直接App的测试和调试，你可以

For immediate application testing and debugging, you can build your application in debug mode and immediately install it on an emulator. In debug mode, the build tools automatically sign your application with a debug key and optimize the package with zipalign.

To build in debug mode:

Open a command-line and navigate to the root of your project directory.
Use Ant to compile your project in debug mode:
ant debug
This creates your debug .apk file inside the project bin/ directory, named <your_project_name>-debug.apk. The file is already signed with the debug key and has been aligned with zipalign.
Each time you change a source file or resource, you must run Ant again in order to package up the latest version of the application.

To install and run your application on an emulator, see the following section about Running on the Emulator.

Building in Release Mode
When you're ready to release and distribute your application to end-users, you must build your application in release mode. Once you have built in release mode, it's a good idea to perform additional testing and debugging with the final .apk.

Before you start building your application in release mode, be aware that you must sign the resulting application package with your private key, and should then align it using the zipalign tool. There are two approaches to building in release mode: build an unsigned package in release mode and then manually sign and align the package, or allow the build script to sign and align the package for you.

Build unsigned

If you build your application unsigned, then you will need to manually sign and align the package.

To build an unsigned .apk in release mode:

Open a command-line and navigate to the root of your project directory.
Use Ant to compile your project in release mode:
ant release
This creates your Android application .apk file inside the project bin/ directory, named <your_project_name>-unsigned.apk.

Note: The .apk file is unsigned at this point and can't be installed until signed with your private key.

Once you have created the unsigned .apk, your next step is to sign the .apk with your private key and then align it with zipalign. To complete this procedure, read Signing Your Applications.

When your .apk has been signed and aligned, it's ready to be distributed to end-users. You should test the final build on different devices or AVDs to ensure that it runs properly on different platforms.

Build signed and aligned

If you would like, you can configure the Android build script to automatically sign and align your application package. To do so, you must provide the path to your keystore and the name of your key alias in your project's ant.properties file. With this information provided, the build script will prompt you for your keystore and alias password when you build in release mode and produce your final application package, which will be ready for distribution.

Caution: Due to the way Ant handles input, the password that you enter during the build process will be visible. If you are concerned about your keystore and alias password being visible on screen, then you may prefer to perform the application signing manually, via Jarsigner (or a similar tool). To instead perform the signing procedure manually, build unsigned and then continue with Signing Your Applications.

To specify your keystore and alias, open the project ant.properties file (found in the root of the project directory) and add entries for key.store and key.alias. For example:

key.store=path/to/my.keystore
key.alias=mykeystore
Save your changes. Now you can build a signed .apk in release mode:

Open a command-line and navigate to the root of your project directory.
Use Ant to compile your project in release mode:
ant release
When prompted, enter you keystore and alias passwords.
Caution: As described above, your password will be visible on the screen.
This creates your Android application .apk file inside the project bin/ directory, named <your_project_name>-release.apk. This .apk file has been signed with the private key specified in ant.properties and aligned with zipalign. It's ready for installation and distribution.

Once built and signed in release mode

Once you have signed your application with a private key, you can install and run it on an emulator or device. You can also try installing it onto a device from a web server. Simply upload the signed .apk to a web site, then load the .apk URL in your Android web browser to download the application and begin installation. (On your device, be sure you have enabled Settings > Applications > Unknown sources.)

Running on the Emulator
Before you can run your application on the Android Emulator, you must create an AVD.

To run your application:

Open the AVD Manager and launch a virtual device
From your SDK's platform-tools/ directory, execute the android tool with the avd options:
android avd
In the Virtual Devices view, select an AVD and click Start.
Install your application
From your SDK's tools/ directory, install the .apk on the emulator:
adb install <path_to_your_bin>.apk
Your .apk file (signed with either a release or debug key) is in your project bin/ directory after you build your application.
If there is more than one emulator running, you must specify the emulator upon which to install the application, by its serial number, with the -s option. For example:
adb -s emulator-5554 install path/to/your/app.apk
To see a list of available device serial numbers, execute adb devices.
If you don't see your application on the emulator, try closing the emulator and launching the virtual device again from the AVD Manager. Sometimes when you install an application for the first time, it won't show up in the application launcher or be accessible by other applications. This is because the package manager usually examines manifests completely only on emulator startup.

Be certain to create multiple AVDs upon which to test your application. You should have one AVD for each platform and screen type with which your application is compatible. For instance, if your application compiles against the Android 4.0 (API Level 14) platform, you should create an AVD for each platform equal to and greater than 4.0 and an AVD for each screen type you support, then test your application on each one.

Tip: If you have only one emulator running, you can build your application and install it on the emulator in one simple step. Navigate to the root of your project directory and use Ant to compile the project with install mode: ant install. This will build your application, sign it with the debug key, and install it on the currently running emulator.

Running on a Device
Before you can run your application on a device, you must perform some basic setup for your device:

Enable USB debugging on your device.
On most devices running Android 3.2 or older, you can find the option under Settings > Applications > Development.
On Android 4.0 and newer, it's in Settings > Developer options.
Note: On Android 4.2 and newer, Developer options is hidden by default. To make it available, go to Settings > About phone and tap Build number seven times. Return to the previous screen to find Developer options.
Ensure that your development computer can detect your device when connected via USB
Read Setting up a Device for Development for more information.

Once your device is set up and connected via USB, navigate to your SDK's platform-tools/ directory and install the .apk on the device:

adb -d install path/to/your/app.apk
The -d flag specifies that you want to use the attached device (in case you also have an emulator running).

For more information on the tools used above, please see the following documents:

android Tool
Android Emulator
Android Debug Bridge (ADB)
Application Signing
As you begin developing Android applications, understand that all Android applications must be digitally signed before the system will install them on an emulator or device. There are two ways to do this: with a debug key (for immediate testing on an emulator or development device) or with a private key (for application distribution).

The Android build tools help you get started by automatically signing your .apk files with a debug key at build time. This means that you can compile your application and install it on the emulator without having to generate your own private key. However, please note that if you intend to publish your application, you must sign the application with your own private key, rather than the debug key generated by the SDK tools.

The ADT plugin helps you get started quickly by signing your .apk files with a debug key, prior to installing them on an emulator or development device. This means that you can quickly run your application from Eclipse without having to generate your own private key. No specific action on your part is needed, provided ADT has access to Keytool. However, please note that if you intend to publish your application, you must sign the application with your own private key, rather than the debug key generated by the SDK tools.

Please read Signing Your Applications, which provides a thorough guide to application signing on Android and what it means to you as an Android application developer. The document also includes a guide to exporting and signing your application with the ADT's Export Wizard.

Ant Command Reference
ant clean
Cleans the project. If you include the all target before clean (ant all clean), other projects are also cleaned. For instance if you clean a test project, the tested project is also cleaned.
ant debug
Builds a debug package. Works on application, library, and test projects and compiles dependencies as needed.
ant emma debug
Builds a test project while building the tested project with instrumentation turned on. This is used to run tests with code coverage enabled.
ant release
Builds a release package.
ant instrument
Builds an instrumented debug package. This is generally called automatically when building a test project with code coverage enabled (with the emma target)
ant <build_target> install
Builds and installs a package. Using install by itself fails.
ant installd
Installs an already compiled debug package. This fails if the .apk is not already built.
ant installr
Installs an already compiled release package. This fails if the .apk is not already built.
ant installt
Installs an already compiled test package. Also installs the .apk of the tested application. This fails if the .apk is not already built.
ant installi
Installs an already compiled instrumented package. This is generally not used manually as it's called when installing a test package. This fails if the .apk is not already built.
ant test
Runs the tests (for test projects). The tested and test .apk files must be previously installed.
ant debug installt test
Builds a test project and the tested project, installs both .apk files, and runs the tests.
ant emma debug install test
Builds a test project and the tested project, installs both .apk files, and runs the tests with code coverage enabled.