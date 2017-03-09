package com.bigman.iotctrl.iotctrl;

import android.util.Log;
import java.util.Collection;

/**
 * Created by BIGMAN on 26/2/2560.
 * from source : scruffyfox
 * https://gist.github.com/scruffyfox/5303003
 */

public class Debug {
    private static long timeseed = 0L;
    private final static String LOG_TAG = "DEBUG";
    private static boolean DEBUG = true;

    public final static String LOG_EVENT = "[EVENT]";
    public final static String LOG_HNDMSG = "[HNDMSG]";
    public final static String LOG_CASTMSG = "[CASTMSG]";
    public final static String LOG_UNDEF =  "[UNDEF]";
    /**
     * Sets if the app is in debug mode.
     *
     * @param inDebug
     *            If set to true then outputs will be made, else they wont
     */
    public static void setDebugMode(boolean inDebug)
    {
        DEBUG = inDebug;
    }

    public static void seedTimer()
    {
        timeseed = System.nanoTime();
    }

    public static long getTick()
    {
        return System.currentTimeMillis();
    }

    public static long tickTimer()
    {
        long newSeed = System.nanoTime();
        long diff = (newSeed - timeseed);
        timeseed = newSeed;
        out("Time: " + (diff / 1000000.0d));
        return diff;
    }

    public static String getCallingStack()
    {
        Throwable fakeException = new Throwable();
        StackTraceElement[] stackTrace = fakeException.getStackTrace();

        if (stackTrace != null && stackTrace.length >= 2)
        {
            String retstr = "";
            int index = 0;
            for (StackTraceElement s : stackTrace)
            {
                if (s != null)
                {
                    retstr += index + " : " + s.getFileName() + " from " + s.getMethodName() + " on line " + s.getLineNumber() + "\n";
                }

                index++;
            }

            return retstr;
        }

        return null;
    }

    public static String getCallingMethodInfo()
    {
        Throwable fakeException = new Throwable();
        StackTraceElement[] stackTrace = fakeException.getStackTrace();

        if (stackTrace != null && stackTrace.length >= 2)
        {
            StackTraceElement s = stackTrace[2];
            if (s != null)
            {
                return s.getFileName() + "(" + s.getMethodName() + ":" + s.getLineNumber() + "):";
            }
        }

        return null;
    }

    private static void longInfo(String str)
    {
        if (str.length() > 4000)
        {
            Log.d(LOG_TAG, str.substring(0, 4000));
            longInfo(str.substring(4000));
        }
        else
        {
            Log.d(LOG_TAG, str);
        }
    }

    public static void out(Collection args)
    {
        if (!DEBUG)
            return;

        String output = "";

        for (Object o : args)
        {
            output += String.valueOf(o) + ", ";
        }

        longInfo(getCallingMethodInfo() + " " + output);
    }

    public static void out(Object... args)
    {
        if (!DEBUG)
            return;

        String output = "";

        for (Object o : args)
        {
            output += String.valueOf(o) + ", ";
        }

        longInfo(getCallingMethodInfo() + " " + output);
    }

    public static void out(String str, Object... args)
    {
        if (!DEBUG)
            return;

        longInfo(getCallingMethodInfo() + " " + String.format(str, args));
    }

    public static void out(Object obj)
    {
        if (!DEBUG)
            return;

        longInfo(getCallingMethodInfo() + " " + (obj == null ? "[null]" : String.valueOf(obj)));
    }

    public static void out(Exception obj)
    {
        if (!DEBUG)
            return;

        obj.printStackTrace();
    }
}
