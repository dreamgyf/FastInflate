package com.dreamgyf.android.plugin.fastinflate.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.dreamgyf.android.plugin.fastinflate.FastInflate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

//        if (System.currentTimeMillis() % 2 == 0L) {
//            val normalStartTime = System.currentTimeMillis()
//            for (i in 0 until 1000) {
//                LayoutInflater.from(this).inflate(R.layout.activity_main, null)
//            }
//            val normalEndTime = System.currentTimeMillis()
//
//            val fastStartTime = System.currentTimeMillis()
//            for (i in 0 until 1000) {
//                FastInflate.from(this).inflate(R.layout.activity_main, null)
//            }
//            val fastEndTime = System.currentTimeMillis()
//
//
//            Log.i("FastInflate", "normalDuration: ${normalEndTime - normalStartTime}")
//            Log.i("FastInflate", "fastDuration: ${fastEndTime - fastStartTime}")
//        } else {
//            val fastStartTime = System.currentTimeMillis()
//            for (i in 0 until 1000) {
//                FastInflate.from(this).inflate(R.layout.activity_main, null)
//            }
//            val fastEndTime = System.currentTimeMillis()
//
//            val normalStartTime = System.currentTimeMillis()
//            for (i in 0 until 1000) {
//                LayoutInflater.from(this).inflate(R.layout.activity_main, null)
//            }
//            val normalEndTime = System.currentTimeMillis()
//
//            Log.i("FastInflate", "fastDuration: ${fastEndTime - fastStartTime}")
//            Log.i("FastInflate", "normalDuration: ${normalEndTime - normalStartTime}")
//        }

        LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        FastInflate.from(this).inflate(R.layout.activity_main, null)

        val normalStartTime = System.currentTimeMillis()
        for (i in 0 until 1000) {
            LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        }
        val normalEndTime = System.currentTimeMillis()

        val fastStartTime = System.currentTimeMillis()
        for (i in 0 until 1000) {
            FastInflate.from(this).inflate(R.layout.activity_main, null)
        }
        val fastEndTime = System.currentTimeMillis()

        Log.i("FastInflate", "normalDuration: ${normalEndTime - normalStartTime}")
        Log.i("FastInflate", "fastDuration: ${fastEndTime - fastStartTime}")

        setContentView(FastInflate.from(this).inflate(R.layout.activity_main, null))
    }
}