package com.google.mynotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@MainActivity ,
                        Notes::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        };thread.start()
    }

    }


