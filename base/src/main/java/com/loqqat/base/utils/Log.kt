package com.loqqat.base.utils

import com.loqqat.base.BuildConfig

class Log {
    companion object {
        @JvmStatic
        fun v(tag: String, msg: String?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.v(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun v(tag: String, msg: String?, tr: Throwable?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.v(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun d(tag: String, msg: String?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.d(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun d(tag: String, msg: String?, tr: Throwable?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.d(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun i(tag: String, msg: String?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.i(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun i(tag: String, msg: String?, tr: Throwable?): Int {
            return android.util.Log.i(tag, msg, tr)
        }

        @JvmStatic
        fun w(tag: String, msg: String?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.w(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun w(tag: String, msg: String?, tr: Throwable?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.w(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun w(tag: String, tr: Throwable?): Int {
            return android.util.Log.w(tag, tr)
        }

        @JvmStatic
        fun e(tag: String, msg: String?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.e(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun e(tag: String, msg: String?, tr: Throwable?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.e(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun wtf(tag: String, msg: String?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.wtf(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun wtf(tag: String, msg: String?, tr: Throwable?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.wtf(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun wtf(tag: String, tr: Throwable?): Int {
            return if (BuildConfig.DEBUG)
                android.util.Log.wtf(tag, tr)
            else
                -1
        }

        fun String.V(tag:String)
        {
            v(tag,this)
        }
    }
}