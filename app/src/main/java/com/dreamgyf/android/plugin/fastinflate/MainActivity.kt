package com.dreamgyf.android.plugin.fastinflate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        setContentView(FastInflate.from(this).inflate(R.layout.activity_main, null))
    }
}