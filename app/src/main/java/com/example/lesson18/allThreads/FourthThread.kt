package com.example.lesson18.allThreads

import android.os.AsyncTask
import com.example.lesson18.ListFunctions

class FourthThread(val listFunctions: ListFunctions, val lock: Object) :
    AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void {
        while (true) {
            if (isCancelled) Thread.interrupted()
            synchronized(lock) {
                lock.wait()
                listFunctions.setMessage("YOP")
            }
        }
    }
}