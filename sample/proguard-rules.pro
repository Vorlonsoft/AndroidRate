# Copyright 2017 Vorlonsoft LLC
#
# Licensed under The MIT License (MIT)

# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt

# Add any project specific keep options here:

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}