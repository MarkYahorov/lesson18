package com.example.lesson18

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class App : Application() {

    companion object{lateinit var INSTANSE: App}

    private var _state: State? = null
    val state: State?
        get() {
            return _state
        }

    override fun onCreate() {
        super.onCreate()

        INSTANSE = this
        addMessageToReceiver()
        setLastNumber()
        setCount()
    }

    private fun addMessageToReceiver(){
        val localBroadcastReceiver = LocalBroadcastManager.getInstance(this)
        val addMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                _state?.addMessage(
                    intent?.getStringExtra(MainActivity.EXTRA_PRIME_NUMBER).toString()
                )
            }
        }

        localBroadcastReceiver.registerReceiver(
            addMessageReceiver, IntentFilter(
                MainActivity.BROADCAST_SAVED_PRIME_NUMBERS
            )
        )
    }

    private fun setLastNumber(){
        val localBroadcastReceiver = LocalBroadcastManager.getInstance(this)
        val lastNumberReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                _state?.setLastNumber(
                    intent?.getIntExtra(MainActivity.EXTRA_LAST_NUMBER, 0) ?: 0
                )
            }
        }
        localBroadcastReceiver.registerReceiver(
            lastNumberReceiver, IntentFilter(
                MainActivity.BROADCAST_SAVED_LAST_NUMBER
            )
        )
    }

    private fun setCount(){
        val localBroadcastReceiver = LocalBroadcastManager.getInstance(this)
        val countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                _state?.setLastCount(
                    intent?.getIntExtra(MainActivity.EXTRA_COUNT, 0) ?: 0
                )
            }
        }
        localBroadcastReceiver.registerReceiver(
            countReceiver, IntentFilter(
                MainActivity.BROADCAST_SAVED_COUNT
            )
        )
    }

    fun createState() {
        _state = State()
    }
}