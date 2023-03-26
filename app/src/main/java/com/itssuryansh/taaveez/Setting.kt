package com.itssuryansh.taaveez

import  android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.taaveez.R
import com.google.taaveez.databinding.ActivitySettingBinding
import com.google.taaveez.databinding.DialogSourceCdeBinding
import java.io.File
import java.io.FileOutputStream
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
           startActivity(Intent(this@Setting, Notes::class.java))
            finish()
            overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_left);

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

        Toast.makeText(this@Setting ,getString(R.string.scroll_down_for_rating), Toast.LENGTH_LONG).show()

        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()))
        startActivity(playStoreIntent)
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
        val i : ImageView = ImageView(applicationContext)
        i.setImageResource(R.drawable.banner)
        val bitmapDrawable = i.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val uri: Uri = getImageToShare(bitmap)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("image/*")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, getPackageName()))
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
    }

    private fun getImageToShare(bitmap: Bitmap): Uri {
        val folder : File = File(cacheDir,"images")
        folder.mkdirs()
        val file: File = File(folder,"shared_image.jpg")
        val fileOutputStream: FileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        val uri: Uri = FileProvider.getUriForFile(applicationContext,"com.itssuryansh.taaveez",file)
        return uri

    }

    override fun onBackPressed() {
        val intent = Intent(this, Notes::class.java)
        startActivity(intent)
        finish()
    }

}