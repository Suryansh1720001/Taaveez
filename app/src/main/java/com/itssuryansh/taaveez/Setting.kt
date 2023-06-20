package com.itssuryansh.taaveez

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import com.itssuryansh.taaveez.databinding.ActivitySettingBinding
import com.itssuryansh.taaveez.databinding.DialogSourceCdeBinding
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

open class Setting : AppCompatActivity() {

    private var binding: ActivitySettingBinding? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        loadswithtoRightMode()
//        binding?.switchTheme?.setChecked(true)
        loadLocate()
        loadDayNight()
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (supportActionBar != null) {
            Toast.makeText(this@Setting, "back setting", Toast.LENGTH_LONG).show()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val typeface: Typeface =
            Typeface.createFromAsset(assets, "arabian_onenighjtstand.ttf")
        binding?.tvNotesHeading?.typeface = typeface

        binding?.btnSettingBack?.setOnClickListener {
            startActivity(Intent(this@Setting, Notes::class.java))
            finish()
            overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_left)
        }

        binding?.llLanguage?.setOnClickListener {
            showChangeLang()
        }

        binding?.llsourceCode?.setOnClickListener {
            sourceCode()
        }

        binding?.llOpenSourceLibrary?.setOnClickListener {
            val link = "https://sites.google.com/view/taaveez-open-source-library/home"
            val intent = Intent(this@Setting, WebView::class.java)
            intent.putExtra(Constants.LINK, link)
            startActivity(intent)
        }

        binding?.llshare?.setOnClickListener {
            share()
        }

        binding?.llfeedback?.setOnClickListener {
            feedback()
        }

        binding?.llPrivacyPolicy?.setOnClickListener {
            val link = "https://sites.google.com/view/taaveez-privacy-policy/home"
            val intent = Intent(this@Setting, WebView::class.java)
            intent.putExtra(Constants.LINK, link)
            startActivity(intent)
        }

        daynight()
    }

    private fun loadswithtoRightMode() {
        if (Constants.Theme == "1") {
            binding?.switchTheme?.setChecked(true)
        } else {
            binding?.switchTheme?.setChecked(false)
        }
    }

    private fun feedback() {
        Toast.makeText(this@Setting, getString(R.string.scroll_down_for_rating), Toast.LENGTH_LONG).show()

        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()))
        startActivity(playStoreIntent)
    }

    private fun aboutDeveloper() {
        val url = "https://suryansh1720001.github.io"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun showChangeLang() {
        val listItems = arrayOf("English", "हिन्दी")
        val mBuilder = AlertDialog.Builder(this@Setting)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialog, which ->
            if (which == 0) {
                setLocate("en")
                recreate()
            } else if (which == 1) {
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
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Setting", MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "MyLang")
        if (language != null) {
            setLocate(language)
        }
    }

    private fun sourceCode() {
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

    // load Dark night after open the app
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun loadDayNight() {
        val sharedPreferences = getSharedPreferences("DayNight", MODE_PRIVATE)
        val DayNight = sharedPreferences.getString("My_DayNight", "MyDayNight")
        if (DayNight != null) {
            setDayNight(DayNight)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setDayNight(daynightMode: String) {
        val editor = getSharedPreferences("DayNight", MODE_PRIVATE).edit()
        editor.putString("My_DayNight", daynightMode)
        editor.apply()
        if (daynightMode == "yes") {
            // set for dark mode
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // load DayNight actually
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun daynight() {
        val switch: Switch
        switch = findViewById(R.id.switchTheme)
        switch.setOnCheckedChangeListener { compoundButton, b ->
            if (switch.isChecked) {
                setDayNight("yes")
                Constants.Theme = "1"
            } else {
                setDayNight("no")
                Constants.Theme = "0"
            }
        }
    }

    private fun share() {
        val i: ImageView = ImageView(applicationContext)
        i.setImageResource(R.drawable.banner)
        val bitmapDrawable = i.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val uri: Uri = getImageToShare(bitmap)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("image/*")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, getPackageName()))
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
    }

    private fun getImageToShare(bitmap: Bitmap): Uri {
        val folder: File = File(cacheDir, "images")
        folder.mkdirs()
        val file: File = File(folder, "shared_image.jpg")
        val fileOutputStream: FileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        val uri: Uri = FileProvider.getUriForFile(applicationContext, "com.itssuryansh.taaveez", file)
        return uri
    }

    override fun onBackPressed() {
        val intent = Intent(this, Notes::class.java)
        startActivity(intent)
        finish()
    }
}
