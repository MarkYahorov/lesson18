package com.example.lesson18.allThreads

import android.os.AsyncTask
import android.util.Log
import com.example.lesson18.ListFunctions

class ThirdThread(
    private val listFunctions: ListFunctions,
    val lock: Object,
    val closeTasks: () -> Unit,
    private val enabledStartBtn: () -> Unit,
    val sendCount: (Int) -> Unit
) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        var count = 0
        if (isCancelled) Thread.interrupted()
        while (count < 10 || count == 10) {
            if (count < 10) {
                listFunctions.setMessage("THIRD ${count++}")
                Log.d("key", "Прошел третий поток")
            } else {
                listFunctions.setMessage("THIRD LAST${count++}")
                Log.d("key", "начало закрытия потоков")
                closeTasks()
            }
            Thread.sleep(500)
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        enabledStartBtn()
    }
}