package com.google.mynotes

//import android.content.Intent
//import android.graphics.Typeface
//import android.net.Uri
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.google.mynotes.databinding.ActivityAboutBinding
//
//class About : AppCompatActivity() {
//    private var binding:ActivityAboutBinding?=null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAboutBinding.inflate(layoutInflater)
//        setContentView(binding?.root)
//
//        val typeface: Typeface =
//            Typeface.createFromAsset(assets,"arabian_onenighjtstand.ttf")
//        binding?.tvNotesHeading?.typeface = typeface
//
//        binding?.tvAbout?.setText(getString(R.string.about_poem))
//
//        binding?.btnAboutBack?.setOnClickListener {
//            finish()
//            overridePendingTransition(R.drawable.slide_in_right,R.drawable.slide_out_rigth);
//
//        }
//
//
//    }
//}



import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        Element()
        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setCustomFont("museo.ttf")
            .setImage(R.mipmap.ic_launcher)
            .setDescription(getString(R.string.about_poem))
            .addItem(Element().setTitle("Current Version : "+getString(R.string.appVersion)).setGravity(Gravity.CENTER).setOnClickListener {
                Toast.makeText(
                    this@About,
                    "Current version of App is : " + getString(R.string.appVersion),
                    Toast.LENGTH_LONG
                ).show()
            })
            .addGroup("CONNECT WITH US!")
            .addEmail("itssuryanshprajapati@gmail.com")
            .addWebsite("https://suryansh1720001.github.io")
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
}