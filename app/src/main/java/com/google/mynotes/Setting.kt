package com.google.mynotes

import  android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.mynotes.databinding.ActivitySettingBinding
import com.google.mynotes.databinding.DialogSourceCdeBinding
import java.util.*

open class Setting : AppCompatActivity() {


    private var binding :ActivitySettingBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES ){
//            setTheme(R.style.Theme_MyNotes_DarkTheme)
//        }else{
//            setTheme(R.style.Theme_MyNotes)
//        }
        super.onCreate(savedInstanceState)
       loadLocate()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (supportActionBar != null){
            Toast.makeText(this@Setting,"back setting",Toast.LENGTH_LONG).show()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val typeface: Typeface =
            Typeface.createFromAsset(assets,"arabian_onenighjtstand.ttf")
        binding?.tvNotesHeading?.typeface = typeface

        binding?.btnSettingBack?.setOnClickListener {
           startActivity(Intent(this@Setting,Notes::class.java))
            finish()
            overridePendingTransition(R.drawable.slide_in_left,R.drawable.slide_out_left);

        }

        binding?.llLanguage?.setOnClickListener {
            showChangeLang()
        }

        binding?.llsourceCode?.setOnClickListener {
            sourceCode()
        }

        binding?.llDeveloper?.setOnClickListener{
            aboutDeveloper()
        }

        binding?.llshare?.setOnClickListener {
            share()
        }

        binding?.llfeedback?.setOnClickListener {
            feedback()
        }



//        binding?.switchTheme?.setOnCheckedChangeListener{ buttonView,isChecked ->
//            if(isChecked){
//                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
////                reset()
//            }
//            else{
//                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
////                reset()
//            }
//        }
    }

    private fun feedback() {
        val url = "https://suryansh1720001.github.io/index.html#contact"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun aboutDeveloper() {
        val url = "https://suryansh1720001.github.io"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

//    private fun reset() {
//        val intent=Intent(this@Setting   ,Setting::class.java)
//        startActivity(intent)
//        finish()
//    }

    private  fun showChangeLang() {
        val listItems = arrayOf("English","हिन्दी")
        val mBuilder = AlertDialog.Builder(this@Setting)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItems,-1){dialog,which->
        if( which ==0){
            setLocate("en")
            recreate()
        }else if(which ==1){
            setLocate("hi")
            recreate()
        }
            dialog.dismiss()
    }
        val mDialog = mBuilder.create()
        mDialog.show()
    }



    private fun setLocate(Lang: String) {

    val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config =Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Setting",Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",Lang)
        editor.apply()

    }

    private fun loadLocate(){
        val sharedPreferences=getSharedPreferences("Setting", Activity.MODE_PRIVATE)
        val language= sharedPreferences.getString("My_Lang","")

        if (language != null) {
            setLocate(language)
        }


    }

    private fun sourceCode(){
        val sourceCodeDialog = Dialog(this)
        sourceCodeDialog.setCancelable(false)
        val binding = DialogSourceCdeBinding.inflate(layoutInflater)
        sourceCodeDialog.setContentView(binding.root)




        binding?.closePopup?.setOnClickListener {
            sourceCodeDialog.dismiss()
        }
        binding?.popupShareBtn?.setOnClickListener {
            share()
        }

        sourceCodeDialog.setCancelable(true)
        sourceCodeDialog.show()
    }


    private fun share() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_subject))
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
    }

}