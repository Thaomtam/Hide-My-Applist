package cn.geektang.privacyspace.util

import android.util.Log
import de.robv.android.xposed.XposedBridge

object Log {
    private const val TAG = "PrivacySpace"
    private var isDebug = false

    fun setDebug(debug: Boolean) {
        isDebug = debug
    }

    fun d(tag: String, message: String) {
        if (isDebug) {
            try {
                Log.d("$TAG:$tag", message)
            } catch (e: Exception) {
                XposedBridge.log("$TAG:$tag: $message")
            }
        }
    }

    fun i(tag: String, message: String) {
        try {
            Log.i("$TAG:$tag", message)
        } catch (e: Exception) {
            XposedBridge.log("$TAG:$tag: $message")
        }
    }

    fun w(tag: String, message: String) {
        try {
            Log.w("$TAG:$tag", message)
        } catch (e: Exception) {
            XposedBridge.log("$TAG:$tag: $message")
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        try {
            if (throwable != null) {
                Log.e("$TAG:$tag", message, throwable)
            } else {
                Log.e("$TAG:$tag", message)
            }
        } catch (e: Exception) {
            XposedBridge.log("$TAG:$tag: $message")
            throwable?.let { XposedBridge.log(it) }
        }
    }
} 