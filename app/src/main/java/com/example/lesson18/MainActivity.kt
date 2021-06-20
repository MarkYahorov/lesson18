package com.example.lesson18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import com.example.lesson18.allThreads.FirstThread
import com.example.lesson18.allThreads.FourthThread
import com.example.lesson18.allThreads.SecondThread
import com.example.lesson18.allThreads.ThirdThread
import java.lang.StringBuilder
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val lock = Object()
    private val list = ListFunctions()

    private lateinit var thisText: TextView
    private lateinit var startBtn: Button

    private lateinit var firstThread: FirstThread
    private lateinit var secondThread: SecondThread
    private lateinit var thirdThread: ThirdThread
    private lateinit var fourthThread: FourthThread

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
            openThirdThread()
            openFourthThread()
        }
    }

    private fun openFirstThread() {
        firstThread = FirstThread(list, setText ={ thisList ->
            thisList.forEach {
                handler.post{thisText.append(it)}
            }
        })
        firstThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }

    private fun openSecondThread() {
        secondThread = SecondThread(list, lock)
        secondThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }

    private fun openThirdThread() {
        thirdThread = ThirdThread(list, lock, closeTasks = {
            firstThread.cancel(true)
            secondThread.cancel(true)
            synchronized(lock){
                lock.notify()
            }
            fourthThread.cancel(true)
        }, enabledStartBtn = {
            startBtn.isEnabled = true
        })
        thirdThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }

    private fun openFourthThread() {
        fourthThread = FourthThread(list, lock)
        fourthThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }

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