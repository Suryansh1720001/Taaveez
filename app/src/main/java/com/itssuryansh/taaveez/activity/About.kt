package com.itssuryansh.taaveez.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.itssuryansh.taaveez.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        loadDayNight()
        loadLocate()
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_about)
        Element()

        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setCustomFont("museo.ttf")
            .setImage(R.mipmap.ic_launcher)
            .setDescription(getString(R.string.about_poem))
            .addItem(Element().setTitle("Current Version : "+getString(R.string.appVersion)).setGravity(Gravity.CENTER).setOnClickListener {

                Toast.makeText(this@About,getString(R.string.About_version) + getString(R.string.appVersion),Toast.LENGTH_LONG).show()

            })
            .addGroup("CONNECT WITH US!")
            .addEmail("itssuryanshprajapati@gmail.com")
            .addWebsite("https://taaveez.vercel.app/")
            .addPlayStore(packageName)
            .addTwitter("itssuryanshP")
            .addYoutube("UCdjJbti71WN9ILx9774q2PA") //Enter your youtube link here (replace with my channel link)
//            .addPlayStore(packageName) //Replace all this with your package name
            .addInstagram("_its_s.u.r.y.a.n.s.h") //Your instagram id

            .addItem(createCopyright())

            .create()

        setContentView(aboutPage)


    }

    private fun createCopyright(): Element {
        val copyright = Element()
        @SuppressLint("DefaultLocale") val copyrightString = String.format(
            "Copyright %d by Suryansh Prajapati",
            Calendar.getInstance()[Calendar.YEAR]
        )
        copyright.title = copyrightString
        copyright.iconDrawable = R.drawable.ic_copyright
        copyright.gravity = Gravity.CENTER
        copyright.onClickListener = View.OnClickListener {
            Toast.makeText(
                this@About,
                copyrightString,
                Toast.LENGTH_SHORT
            ).show()
        }
        return copyright
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_rigth);
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun loadDayNight(){
        val sharedPreferences=getSharedPreferences("DayNight", Activity.MODE_PRIVATE)
        val DayNight= sharedPreferences.getString("My_DayNight","MyDayNight")
        if (DayNight != null) {
            setDayNight(DayNight)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setDayNight(daynightMode: String) {
        val editor = getSharedPreferences("DayNight", Context.MODE_PRIVATE).edit()
        editor.putString("My_DayNight",daynightMode)
        editor.apply()
        if(daynightMode=="yes"){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    private fun loadLocate(){
        val sharedPreferences=getSharedPreferences("Setting", Activity.MODE_PRIVATE)
        val language= sharedPreferences.getString("My_Lang","MyLang")
        if (language != null) {
            setLocate(language)
        }
    }

    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Setting", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",Lang)
        editor.apply()
    }


}