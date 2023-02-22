package com.hw.kotlin.demo.structs

import android.util.Log

class PersonView {
    /**
     * 反例 永远不要使用Unit?作为返回值，容易让人疑惑
     */
    fun showPerson(person: Person) : Unit?{
        Log.i(TAG, "showPerson person = $person")
        return null
    }

    fun showError() {
        Log.i(TAG, "showError")

        val txt = (1..10).joinToString(separator = "|", prefix = "<", postfix = ">")

        Log.i(TAG, "showError txt = $txt")
    }


    fun Iterable<Int>.product() = fold(1) { acc, i ->
        acc * i
    }


    companion object {
        private const val TAG = "PersonView"
    }
}