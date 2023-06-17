package com.itssuryansh.taaveez

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itssuryansh.taaveez.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//     Glide.with(this@MainActivity).load(R.drawable.gif_book).into(binding?.ivBookGif!!)     // For GIF
        val typeface: Typeface =
            Typeface.createFromAsset(assets, "arabian_onenighjtstand.ttf")
        binding?.tvNotes?.typeface = typeface

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(
                        this@SplashActivity,
                        Notes::class.java,
                    )
                    startActivity(intent)

                    finish()
                    overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_left)
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }; thread.start()
    }
}
