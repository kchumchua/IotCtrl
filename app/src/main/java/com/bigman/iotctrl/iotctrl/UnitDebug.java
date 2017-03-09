package com.bigman.iotctrl.iotctrl;

import android.util.Log;

import java.util.Collection;

/**
 * Created by BIGMAN on 26/2/2560.
 */

public class UnitDebug extends Debug {
    private String TAGNAME  = "DEBUG";
    private boolean DEBUG = true;

    UnitDebug(String tagname,boolean enb)
    {
        TAGNAME = tagname;
        DEBUG = enb;
    }
    UnitDebug(boolean enb)
    {
        this("DEBUG",enb);
    }

    public void setEnable(boolean end)
    {
        DEBUG = end;
    }


    public static String getCallMethodInfo(Integer lvl)
    {
        Throwable fakeException = new Throwable();
        StackTraceElement[] stackTrace = fakeException.getStackTrace();
        lvl = lvl>=2?lvl:2;
        if (stackTrace != null && stackTrace.length >= lvl)
        {
            StackTraceElement s = stackTrace[lvl];
            if (s != null)
            {
                return s.getFileName() + "(" + s.getMethodName() + ":" + s.getLineNumber() + "):";
            }
        }

        return null;
    }

    private void logInfo(String tag,String str)
    {
        if (str.length() > 4000)
        {
            Log.d(tag, str.substring(0, 4000));
            logInfo(tag,str.substring(4000));
        }
        else
        {
            Log.d(tag, str);
        }
    }


    public  void log(Collection args)
    {
        if (!DEBUG)
            return;

        String output = "";

        for (Object o : args)
        {
            output += String.valueOf(o) + ", ";
        }

        logInfo(TAGNAME,getCallMethodInfo(2) + " " + output);
    }

    public  void log(Object... args)
    {
        if (!DEBUG)
            return;

        String output = "";

        for (Object o : args)
        {
            output += String.valueOf(o) + ", ";
        }

        logInfo(TAGNAME,getCallMethodInfo(2) + " " + output);
    }

    public void log(String str, Object... args)
    {
        if (!DEBUG)
            return;

        logInfo(TAGNAME,getCallMethodInfo(2) + " " + String.format(str, args));
    }

    public  void log(Object obj)
    {
        if (!DEBUG)
            return;

        logInfo(TAGNAME,getCallMethodInfo(2) + " " + (obj == null ? "[null]" : String.valueOf(obj)));
    }

    public  void log(Exception obj)
    {
        if (!DEBUG)
            return;

        obj.printStackTrace();
    }
}
