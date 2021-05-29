package com.example.taggingapplication.utilities

import android.util.Log
import com.example.taggingapplication.BuildConfig

class AppLogger {
    companion object {
        fun errorLog(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message)
            }
        }
    }
}