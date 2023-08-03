package com.itssuryansh.taaveez.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.itssuryansh.taaveez.R
import com.itssuryansh.taaveez.adapter.OneTimeIntroPagerAdapter


class OneTimeIntroActivity : AppCompatActivity(), OneTimeIntroPagerAdapter.ViewPagerAdapterListener{



    private lateinit var mSlideViewPager: ViewPager
    private lateinit var next: FrameLayout
    private lateinit var skipbtn: FrameLayout

    private lateinit var dots: Array<TextView>
    private lateinit var viewPagerAdapter: OneTimeIntroPagerAdapter


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the application is running for the first time
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val isFirstRun = preferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            setContentView(R.layout.one_time_intro_activity)

            skipbtn = findViewById(R.id.skipButton)


            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR


            window.statusBarColor = ContextCompat.getColor(this, R.color.white)


            skipbtn.setOnClickListener {
                val i = Intent(this@OneTimeIntroActivity, SplashActivity::class.java)
                startActivity(i)
                finish()
            }

            mSlideViewPager = findViewById(R.id.slideViewPager)


            viewPagerAdapter = OneTimeIntroPagerAdapter(this)


            mSlideViewPager.adapter = viewPagerAdapter

            viewPagerAdapter.setListener(this)

            // Set a flag indicating that the splash screen has been shown
            val editor = preferences.edit()
            editor.putBoolean("isFirstRun", false)
            editor.apply()

        } else {
            // If it's not the first run, directly start the MainActivity
            val intent = Intent(this@OneTimeIntroActivity,SplashActivity::class.java)
            startActivity(intent)
            finish()

        }

    }

    private fun getItem(i: Int): Int {

        return mSlideViewPager.currentItem + i

    }

    override fun onReadyButtonClick() {
        if (getItem(0) < 2)
            mSlideViewPager.setCurrentItem(getItem(1), true)
        else {
            val i = Intent(this@OneTimeIntroActivity, SplashActivity::class.java)
            startActivity(i)
            finish()
        }
    }


}
