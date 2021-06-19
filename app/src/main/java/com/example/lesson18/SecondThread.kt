package com.example.lesson18

import android.os.AsyncTask
import android.util.Log

class SecondThread(
    val listFunctions: ListFunctions,
    val lock: Object
) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void {
        var nextPrimeNumber = 2
        while (true) {
            var dividerCount = 0
            for (i in nextPrimeNumber..400) {
                if (nextPrimeNumber % i == 0) {
                    dividerCount++
                }
            }
            if (dividerCount < 2) {
                synchronized(lock) {
                    listFunctions.setMessage("SECOND THREAD $nextPrimeNumber")
                    Log.d("key", "second $nextPrimeNumber")
                    lock.notify()
                }
            }
            nextPrimeNumber++
            Thread.sleep(200)
        }
    }
}