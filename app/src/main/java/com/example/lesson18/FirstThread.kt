package com.example.lesson18

import android.os.AsyncTask
import android.util.Log

class FirstThread(val list:ListFunctions,
                  val setText:(MutableList<String>) -> Unit
                  ):AsyncTask<Void,MutableList<String>,MutableList<String>>() {
    override fun doInBackground(vararg params: Void?): MutableList<String> {
        while (true){
            Thread.sleep(100)
            list.getMessages().let { thisList ->
                publishProgress(thisList)
            }
            Log.d("key", "Прошел первый поток")
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