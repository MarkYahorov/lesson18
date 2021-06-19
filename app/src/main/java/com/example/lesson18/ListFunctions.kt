package com.example.lesson18

class ListFunctions {

    @Volatile
    private var list = mutableListOf<String>()
    private var lock = Object()

    fun setMessage(message: String){
        synchronized(lock){
            list.add(message)
        }
    }

    fun getMessages():MutableList<String>{
        synchronized(lock){
            val secondList = mutableListOf<String>()
            list.forEach {
                secondList.add(it)
            }
            list.clear()
            return secondList
        }
    }
}