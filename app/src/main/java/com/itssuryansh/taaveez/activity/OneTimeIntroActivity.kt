package com.itssuryansh.taaveez.activity
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import com.github.appintro.AppIntro
//import com.github.appintro.AppIntroFragment
//import com.github.appintro.AppIntroPageTransformerType
//import com.itssuryansh.taaveez.Constants
//import com.itssuryansh.taaveez.R
//
//
//class OneTimeIntroActivity : AppIntro(){
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        //vibration add
//        isVibrate = true
//        vibrateDuration = 50L
//
//        // Check if the app is running for the first time
//        val sharedPreferences = getSharedPreferences(Constants.MyPrefs, Context.MODE_PRIVATE)
//        val introShown = sharedPreferences.getBoolean(Constants.introShown, false)    // it is false initially
//        if (introShown) {
//            // The intro screen has already been shown, so just finish this activity
//            startActivity(Intent(this@OneTimeIntroActivity, SplashActivity::class.java))
//            finish()
//            return
//        }
//
//
//       // Hide/Show the status Bar
//        showStatusBar(true)
//
//        // Control the navigation bar color
//        setTransformer(AppIntroPageTransformerType.Fade)
//
//        setColorSkipButton(Color.WHITE)
//        setColorDoneText(Color.WHITE)
//        setNextArrowColor(Color.WHITE)
//        setBackArrowColor(Color.WHITE)
//        setNavBarColor(Color.WHITE)
//
//
//        addSlide(AppIntroFragment.createInstance(
//            title = "वो दुआएं, वो गीत, वो किस्से-कहानियां जो अधूरी रह गई वो सब आप इस तावीज़ में रख सकते हैं जाने कब कौन सी दुआ क़ुबूल हो जाए।\n",
//            description = "Say hello to Taaveez...",
//            backgroundDrawable = R.drawable.app_intro_first_bg,
//            titleColorRes = R.color.white,
//            descriptionColorRes = R.color.white,
//
//            ))
//
////        ..Let's get started!
//
//        addSlide(AppIntroFragment.createInstance(
//            title = "A digital charm for creators to please,\n" +
//                    "Taaveez unlocks their artistic expertise,\n" +
//                    "Great content flows with effortless ease.",
//            description = "The innovative and thrilling application tailored for artists and entertainers :)",
//
//            backgroundDrawable = R.drawable.app_intro_second_bg,
//            titleColorRes = R.color.white,
//            descriptionColorRes = R.color.white,
//            backgroundColorRes = R.color.black,
//
//            ))
//        val editor = sharedPreferences.edit()
//        editor.putBoolean(Constants.introShown, true)
//        editor.apply()
//
//    }
//
//
//    override fun onSkipPressed(currentFragment: Fragment?) {
//        super.onSkipPressed(currentFragment)
//        // Decide what to do when the user clicks on "Skip"
//        startActivity(Intent(this@OneTimeIntroActivity, HomePage::class.java ))
//        finish()
//    }
//
//    override fun onDonePressed(currentFragment: Fragment?) {
//        super.onDonePressed(currentFragment)
//        // Decide what to do when the user clicks on "Done"
//        val intent = Intent(this@OneTimeIntroActivity, HomePage::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//}



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
