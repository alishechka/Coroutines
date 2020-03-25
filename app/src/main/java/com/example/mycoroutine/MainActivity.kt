package com.example.mycoroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    //added some stuff to handle onDestroy or phone tilting
    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()


        btn_start_countdown.setOnClickListener {
            compute(et_countdown.text.toString().toInt())
        }
    }

    fun compute(num: Int) = runBlocking {
        val job = GlobalScope.launch {
                foo(num).collect { value -> tv_display_timer.text = value.toString() }
                tv_display_timer.text = "Done!"
        }

        //to stop operation
        btn_stop_countdown.setOnClickListener {
            job.cancel()
            tv_display_timer.text = "STOP!"
        }
    }

    suspend fun foo(num: Int): Flow<Int> = flow {
        for (i in num downTo 0) {
            delay(500L)
            emit(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}

