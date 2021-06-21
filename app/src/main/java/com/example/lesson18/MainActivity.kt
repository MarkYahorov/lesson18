package com.example.lesson18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.lesson18.allThreads.FirstThread
import com.example.lesson18.allThreads.FourthThread
import com.example.lesson18.allThreads.SecondThread
import com.example.lesson18.allThreads.ThirdThread
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val lock = Object()
    private val list = ListFunctions()
    private val handler = Handler(Looper.getMainLooper())
    private var lastCalculatedNumber = 2
    private var lastCount = 1

    private lateinit var thisText: TextView
    private lateinit var startBtn: Button

    private lateinit var firstThread: FirstThread
    private lateinit var secondThread: SecondThread
    private lateinit var thirdThread: ThirdThread
    private lateinit var fourthThread: FourthThread

    @Volatile
    private var messageList = mutableListOf<String>()

    @Volatile
    var isRunning = false

    companion object {
        const val EXTRA_PRIME_NUMBER = "PRIME_NUMBER"
        const val EXTRA_COUNT = "COUNT"
        const val EXTRA_LAST_NUMBER = "LAST_NUMBER"
        const val BROADCAST_SAVED_PRIME_NUMBERS = "SAVED_PRIME_NUMBERS"
        const val BROADCAST_SAVED_COUNT = "SAVED_COUNT"
        const val BROADCAST_SAVED_LAST_NUMBER = "SAVED_LAST_NUMBER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAll()
        if (App.INSTANSE.state == null) {
            App.INSTANSE.createState()
        } else {
            initDataAfterRotate()
        }
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
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        firstThread = FirstThread(list) {
            it.forEach { text ->
                localBroadcastManager.sendBroadcastSync(Intent(BROADCAST_SAVED_PRIME_NUMBERS).apply {
                    putExtra(EXTRA_PRIME_NUMBER, text)
                })
                handler.post { thisText.append(text) }
            }
        }
        firstThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }


    private fun openSecondThread() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        secondThread = SecondThread(list, lock) {
            localBroadcastManager.sendBroadcastSync(Intent(BROADCAST_SAVED_LAST_NUMBER).apply {
                putExtra(EXTRA_LAST_NUMBER, it)
            })
        }
        secondThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }

    private fun openThirdThread() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        thirdThread = ThirdThread(list, lock, closeTasks = {
            firstThread.cancel(true)
            secondThread.cancel(true)
            synchronized(lock) {
                lock.notify()
            }
            fourthThread.cancel(true)
        }, enabledStartBtn = {
            startBtn.isEnabled = true
        }, sendCount = {
            localBroadcastManager
                .sendBroadcastSync(Intent(BROADCAST_SAVED_COUNT).apply {
                    putExtra(EXTRA_COUNT, it)
                })
        })
        thirdThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }

    private fun openFourthThread() {
        fourthThread = FourthThread(list, lock)
        fourthThread.executeOnExecutor(Executors.newFixedThreadPool(4))
    }

    private fun initDataAfterRotate() {
        App.INSTANSE.state?.let {
            lastCalculatedNumber = it.lastNumber + 1
            lastCount = it.lastCount + 1
            it.list.forEach { listValue ->
                thisText.append(listValue)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        firstThread.cancel(true)
        secondThread.cancel(true)
        thirdThread.cancel(true)
        fourthThread.cancel(true)
        startBtn.setOnClickListener(null)
    }
}