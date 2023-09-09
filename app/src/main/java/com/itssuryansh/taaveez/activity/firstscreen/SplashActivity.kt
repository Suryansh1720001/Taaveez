package com.itssuryansh.taaveez.activity.firstscreen

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.itssuryansh.taaveez.R
import com.itssuryansh.taaveez.activity.HomePage

import com.itssuryansh.taaveez.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding?=null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivitySplashBinding.inflate(layoutInflater)



        // Set the status bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.white)
        }

        setContentView(binding?.root)





//     Glide.with(this@MainActivity).load(R.drawable.gif_book).into(binding?.ivBookGif!!)     // For GIF
        val typeface: Typeface =
            Typeface.createFromAsset(assets,"museo.ttf")
        binding?.tvTaaveezHeading?.typeface = typeface

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@SplashActivity ,
                        HomePage::class.java)
                    startActivity(intent)

                    finish()
                    overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_rigth);
//                    overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out)
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                }
            }
        };thread.start()
    }

    }


