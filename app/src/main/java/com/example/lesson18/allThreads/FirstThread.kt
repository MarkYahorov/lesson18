package com.example.lesson18.allThreads

import android.os.AsyncTask
import com.example.lesson18.ListFunctions

class FirstThread(val list: ListFunctions,
                  val setText:(MutableList<String>) -> Unit
                  ):AsyncTask<Void,MutableList<String>,MutableList<String>>() {
    override fun doInBackground(vararg params: Void?): MutableList<String> {
        while (true){
            if (isCancelled) Thread.interrupted()
            Thread.sleep(100)
            list.getMessages().let { thisList ->
                publishProgress(thisList)
            }
        }
    }

    override fun onProgressUpdate(vararg values: MutableList<String>?) {
        super.onProgressUpdate(*values)
        Thread.sleep(100)
        values[0]?.let {
            setText(it)
        }
    }
}