package com.example.lesson18

import android.app.Application

class State {

    private val _list = mutableListOf<String>()
    val list: MutableList<String>
        get() {
            return _list
        }

    private var _lastNumber = 0
    val lastNumber: Int
        get() {
            return _lastNumber
        }

    private var _lastCount: Int = 0
    val lastCount: Int
        get() {
            return _lastCount
        }

    fun addMessage(message: String) {
        _list.add(message)
    }

    fun setLastNumber(thisNumber: Int) {
        _lastNumber = thisNumber
    }

    fun setLastCount(thisCount: Int) {
        _lastCount = thisCount
    }
}