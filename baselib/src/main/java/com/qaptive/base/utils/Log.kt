package com.qaptive.base.utils


class Log {
    companion object {
        
        private var isDebug :Boolean = false

        @JvmStatic
        fun init(isDebug:Boolean){
            this.isDebug = isDebug
        }
        
        @JvmStatic
        fun v(tag: String, msg: String): Int {
            return if (isDebug)
                android.util.Log.v(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun v(tag: String, msg: String?, tr: Throwable?): Int {
            return if (isDebug)
                android.util.Log.v(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun d(tag: String, msg: String): Int {
            return if (isDebug)
                android.util.Log.d(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun d(tag: String, msg: String?, tr: Throwable?): Int {
            return if (isDebug)
                android.util.Log.d(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun i(tag: String, msg: String): Int {
            return if (isDebug)
                android.util.Log.i(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun i(tag: String, msg: String?, tr: Throwable?): Int {
            return android.util.Log.i(tag, msg, tr)
        }

        @JvmStatic
        fun w(tag: String, msg: String): Int {
            return if (isDebug)
                android.util.Log.w(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun w(tag: String, msg: String?, tr: Throwable?): Int {
            return if (isDebug)
                android.util.Log.w(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun w(tag: String, tr: Throwable?): Int {
            return android.util.Log.w(tag, tr)
        }

        @JvmStatic
        fun e(tag: String, msg: String): Int {
            return if (isDebug)
                android.util.Log.e(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun e(tag: String, msg: String?, tr: Throwable?): Int {
            return if (isDebug)
                android.util.Log.e(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun wtf(tag: String, msg: String?): Int {
            return if (isDebug)
                android.util.Log.wtf(tag, msg)
            else
                -1
        }

        @JvmStatic
        fun wtf(tag: String, msg: String?, tr: Throwable?): Int {
            return if (isDebug)
                android.util.Log.wtf(tag, msg, tr)
            else
                -1
        }

        @JvmStatic
        fun wtf(tag: String, tr: Throwable): Int {
            return if (isDebug)
                android.util.Log.wtf(tag, tr)
            else
                -1
        }

        fun String.log(tag:String)
        {
            v(tag,this)
        }

        fun Throwable.log(tag:String){
            wtf(tag,this)
        }
    }
}