package com.itssuryansh.taaveez

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class OneTimeIntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // vibration add
        isVibrate = true
        vibrateDuration = 50L

        // Check if the app is running for the first time
        val sharedPreferences = getSharedPreferences(Constants.MyPrefs, Context.MODE_PRIVATE)
        val introShown = sharedPreferences.getBoolean(Constants.introShown, false) // it is false initially
        if (introShown) {
            // The intro screen has already been shown, so just finish this activity
            startActivity(Intent(this@OneTimeIntroActivity, SplashActivity::class.java))
            finish()
            return
        }

        // Hide/Show the status Bar
        showStatusBar(true)

        // Control the navigation bar color
        setTransformer(AppIntroPageTransformerType.Fade)

        setColorSkipButton(Color.WHITE)
        setColorDoneText(Color.WHITE)
        setNextArrowColor(Color.WHITE)
        setBackArrowColor(Color.WHITE)
        setNavBarColor(Color.WHITE)

        addSlide(
            AppIntroFragment.createInstance(
                title = "वो दुआएं, वो गीत, वो किस्से-कहानियां जो अधूरी रह गई वो सब आप इस तावीज़ में रख सकते हैं जाने कब कौन सी दुआ क़ुबूल हो जाए।\n",
                description = "Say hello to Taaveez...",
                backgroundDrawable = R.drawable.app_intro_first_bg,
                titleColorRes = R.color.white,
                descriptionColorRes = R.color.white,

            ),
        )

//        ..Let's get started!

        addSlide(
            AppIntroFragment.createInstance(
                title = "A digital charm for creators to please,\n" +
                    "Taaveez unlocks their artistic expertise,\n" +
                    "Great content flows with effortless ease.",
                description = "The innovative and thrilling application tailored for artists and entertainers :)",

                backgroundDrawable = R.drawable.app_intro_second_bg,
                titleColorRes = R.color.white,
                descriptionColorRes = R.color.white,
                backgroundColorRes = R.color.black,

            ),
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(Constants.introShown, true)
        editor.apply()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        startActivity(Intent(this@OneTimeIntroActivity, Notes::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val intent = Intent(this@OneTimeIntroActivity, Notes::class.java)
        startActivity(intent)
        finish()
    }
}
