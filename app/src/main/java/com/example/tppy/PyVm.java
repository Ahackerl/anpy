package com.example.tppy;

import android.util.Log;

public class PyVm {

    static {
        System.loadLibrary("tppy");
    }

    public static native void initVM();
    public static native void lazyModules(String moduleSource, String moduleName);
    public static native void exec(String source);

    //JNI Call
    public static void onPyVMCallback(byte[] data) {

        String s = new String(data);
        Log.d("PyVm",s);

    }

}
