# Copyright 2017 - 2018 Vorlonsoft LLC
#
# Licensed under The MIT License (MIT)

-keep class com.vorlonsoft.android.rate.** { *; }
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}