# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:/androidSDK/adt-bundle-windows-x86_64/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepparameternames

-keep public class * extends android.content.BroadcastReceiver {public *; protected *;}
-keep public class * extends com.ibm.mce.sdk.wi.WakefulIntentService {public *; protected *;}
-keep public class * implements com.ibm.mce.sdk.job.MceSdkJob {public *; protected *;}
-keep public class * implements com.ibm.mce.sdk.job.MceSdkOneTimeJob {public *; protected *;}
-keep public class * implements com.ibm.mce.sdk.job.MceSdkRepeatingJob {public *; protected *;}
-keep public class * extends android.app.IntentService {public *; protected *;}
-keep public class * extends android.app.Service {public *; protected *;}
-keep public class com.ibm.mce.sdk.api.** {public *; protected *;}
-keep public class com.ibm.mce.sdk.events.** {public *; protected 
-keep public class com.ibm.mce.sdk.attributes.** {public *; protected *;}
-keep public class com.ibm.mce.sdk.db.** {public *; protected *;}
-keep public class com.ibm.mce.sdk.encryption.** {public *; protected *;}
-keep public class com.ibm.mce.sdk.notification.NotificationsUtility {public *; protected *;}
-keep public class com.ibm.mce.sdk.notification.NotificationsUtility$* {public *; protected *;}
-keep public class com.ibm.mce.sdk.Preferences {public *; protected *;}
-keep public class com.amazon.device.messaging.ADM {public *; protected *;}

-keep public class com.ibm.mce.sdk.adm.AdmDeliveryChannel {public *; protected *;}
-keep public class com.ibm.mce.sdk.gcm.GcmDeliveryChannel {public *; protected *;}
-keep public class com.google.android.gms.common.GooglePlayServicesUtil {public *; protected *;}

-keep public class * extends android.content.ContentProvider
-keep public class * extends com.ibm.mce.sdk.api.notification.MceNotificationAction


-keep class com.ibm.mce.sdk.** { *; }
-dontwarn com.ibm.mce.sdk.**
-keep public class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontwarn javax.**
-keeppackagenames com.android.providers.contacts.**
#end