package com.google.mynotes

import  android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.LocaleList
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import com.google.mynotes.databinding.ActivityMainBinding
import com.google.mynotes.databinding.ActivitySettingBinding
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


        binding?.btnSettingBack?.setOnClickListener {
            finish()
        }

        binding?.llLanguage?.setOnClickListener {
            showChangeLang()
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

}