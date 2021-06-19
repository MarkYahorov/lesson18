package com.example.lesson18

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.lang.StringBuilder
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val lock = Object()
    private val list = ListFunctions()

    private lateinit var thisText: TextView
    private lateinit var startBtn: Button

    private lateinit var firstThread: FirstThread
    private lateinit var secondThread: SecondThread
//    private lateinit var thirdThread: ThirdThread
//    private lateinit var fourthThread: FourthThread

    private val handler = Handler(Looper.getMainLooper())
    private val stringBuilder = StringBuilder()

    @Volatile
    private var messageList = mutableListOf<String>()

    @Volatile
    var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAll()
    }

    private fun initAll() {
        thisText = findViewById(R.id.this_text)
        startBtn = findViewById(R.id.start_btn)
    }

    override fun onStart() {
        super.onStart()
        setListener()
        thisText.movementMethod = ScrollingMovementMethod()
    }

    private fun setListener() {
        startBtn.setOnClickListener {
            startBtn.isEnabled = false
            isRunning = true
            openFirstThread()
            openSecondThread()
//            openThirdThread()
//            openFourthThread()
        }
    }

    private fun openFirstThread() {
        firstThread = FirstThread(list, setText ={ thisList ->
            thisList.forEach {
                handler.post{thisText.append(it)}
            }
        })
        firstThread.execute()
    }

    private fun openSecondThread() {
        secondThread = SecondThread(list, lock)
        secondThread.execute()
    }

//    private fun openThirdThread() {
//        thirdThread = Thread {
//            var count = 0
//            while (count < 10 || count == 10) {
//                if (count < 10) {
//                    addMessageToList("ФЫВФЫВ${count++}")
//                    Thread.sleep(5000)
//                } else {
//                    addMessageToList("asd${count++}")
//                    isRunning = false
//                    firstThread.join()
//                    Log.d("key", "Поток1 завершил работу ")
//                    secondThread.join()
//                    Log.d("key", "Поток2 завершил работу ")
//                    synchronized(lock) {
//                        lock.notify()
//                    }
//                    fourthThread.join()
//                    Log.d("key", "Поток4 завершил работу ")
//                    handler.post { startBtn.isEnabled = true }
//                }
//            }
//        }
//        thirdThread.start()
//    }
//
//    private fun openFourthThread() {
//        fourthThread = Thread {
//            synchronized(lock) {
//                while (isRunning) {
//                    lock.wait()
//                    addMessageToList("YOP")
//                }
//
//            }
//        }
//        fourthThread.start()
//    }

    private fun addMessageToList(message: String) {
        synchronized(lock) {
            messageList.add(message)
        }
    }

    override fun onStop() {
        super.onStop()
        startBtn.setOnClickListener(null)
    }
}