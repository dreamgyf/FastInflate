package com.dreamgyf.android.plugin.fastinflate.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import com.dreamgyf.android.plugin.fastinflate.FastInflate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //排除缓存影响
        for (i in 0 until 1000) {
            LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        }
        for (i in 0 until 1000) {
            FastInflate.from(this).inflate(R.layout.activity_main, null)
        }

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

        val root = findViewById<View>(R.id.root)

        val tagValue = root.getTag(R.id.tag)

        assert(tagValue == "#ff000000")

        val viewStub = findViewById<ViewStub>(R.id.view_stub)
        viewStub.inflate()
    }
}