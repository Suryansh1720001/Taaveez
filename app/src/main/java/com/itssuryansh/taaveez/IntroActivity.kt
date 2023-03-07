package com.itssuryansh.taaveez

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.google.taaveez.R


class IntroActivity : AppIntro(){
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isVibrate = true
        vibrateDuration = 50L
        showStatusBar(true)
// Control the status bar color


// Control the navigation bar color
        setNavBarColor(Color.RED)
        setNavBarColorRes(R.color.blue_shade_2)
        setTransformer(AppIntroPageTransformerType.Fade)


        // Toggle Indicator Visibility
        isIndicatorEnabled = true

// Change Indicator Color
        setIndicatorColor(
            selectedIndicatorColor = getColor(R.color.blue_shade_1),
            unselectedIndicatorColor = getColor(R.color.blue_shade_2)
        )

// Switch from Dotted Indicator to Progress Indicator
        setProgressIndicator()
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.createInstance(
            title = "Welcome...",
            description = "This is the first slide of the example",
            imageDrawable = R.drawable.iv_setting,
            backgroundDrawable = R.drawable.heading_bg,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.blue_shade_2,
            backgroundColorRes = R.color.black,


            ))
        addSlide(AppIntroFragment.createInstance(
            title = "...Let's get started!",
            description = "This is the last slide, I won't annoy you more :)",
            imageDrawable = R.drawable.front_ui_image,
            backgroundDrawable = R.drawable.bottom_bg,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.blue_shade_2,
            backgroundColorRes = R.color.black,

            ))

    }


    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        startActivity(Intent(this@IntroActivity,Notes::class.java ))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val intent = Intent(this@IntroActivity,Notes::class.java)
        startActivity(intent)
        finish()
    }
}