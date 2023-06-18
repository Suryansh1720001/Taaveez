package com.itssuryansh.taaveez

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.Calendar

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        loadDayNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        Element()

        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setCustomFont("museo.ttf")
            .setImage(R.mipmap.ic_launcher)
            .setDescription(getString(R.string.about_poem))
            .addItem(
                Element().setTitle("Current Version : " + getString(R.string.appVersion)).setGravity(Gravity.CENTER).setOnClickListener {
                    Toast.makeText(
                        this@About,
                        "Current version of App is : " + getString(R.string.appVersion),
                        Toast.LENGTH_LONG,
                    ).show()
                },
            )
            .addGroup("CONNECT WITH US!")
            .addEmail("itssuryanshprajapati@gmail.com")
            .addWebsite("https://suryansh1720001.github.io")
            .addYoutube("UCdjJbti71WN9ILx9774q2PA") // Enter your youtube link here (replace with my channel link)
//            .addPlayStore(packageName) //Replace all this with your package name
            .addInstagram("_its_s.u.r.y.a.n.s.h") // Your instagram id
            .addItem(createCopyright())
            .create()

        setContentView(aboutPage)
    }

    private fun createCopyright(): Element {
        val copyright = Element()

        @SuppressLint("DefaultLocale")
        val copyrightString = String.format(
            "Copyright %d by Suryansh Prajapati",
            Calendar.getInstance()[Calendar.YEAR],
        )
        copyright.title = copyrightString
        copyright.iconDrawable = R.drawable.ic_copyright
        copyright.gravity = Gravity.CENTER
        copyright.onClickListener = View.OnClickListener {
            Toast.makeText(
                this@About,
                copyrightString,
                Toast.LENGTH_SHORT,
            ).show()
        }
        return copyright
    }

    override fun onBackPressed() {
        val intent = Intent(this, Notes::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadDayNight() {
        val sharedPreferences = getSharedPreferences("DayNight", Activity.MODE_PRIVATE)
        val DayNight = sharedPreferences.getString("My_DayNight", "MyDayNight")
        if (DayNight != null) {
            setDayNight(DayNight)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setDayNight(daynightMode: String) {
        val editor = getSharedPreferences("DayNight", Context.MODE_PRIVATE).edit()
        editor.putString("My_DayNight", daynightMode)
        editor.apply()
        if (daynightMode == "yes") {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
