# Copyright 2018 Vorlonsoft LLC
#
# Licensed under The MIT License (MIT)

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}

-dontwarn java.lang.invoke.**