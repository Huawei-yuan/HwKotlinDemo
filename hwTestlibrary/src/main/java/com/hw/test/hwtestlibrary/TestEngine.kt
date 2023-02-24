package com.hw.test.hwtestlibrary

import android.util.Log

class TestEngine private constructor(){

    init {
        Log.i(TAG, "Initing")
    }

    fun sayHello() {
        Log.i(TAG, "Hello, I\'m TestEngine")
    }

    companion object {
        private const val TAG = "TestEngine"

        val instance: TestEngine by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            TestEngine()
        }
    }
}