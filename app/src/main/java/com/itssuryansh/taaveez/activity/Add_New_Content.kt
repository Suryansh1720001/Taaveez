package com.itssuryansh.taaveez.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.*
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.itssuryansh.taaveez.TaaveezApp
import com.itssuryansh.taaveez.TaaveezDao
import com.itssuryansh.taaveez.TaaveezEntity
import com.itssuryansh.taaveez.R
import com.itssuryansh.taaveez.databinding.ActivityAddNewContentBinding
import com.itssuryansh.taaveez.databinding.DialogBinding
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Add_New_Content : AppCompatActivity() {

    private var binding: ActivityAddNewContentBinding? = null

    private var isContentCompleteStatus : Boolean = false



    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocate()
        loadDayNight()
        super.onCreate(savedInstanceState)

        binding = ActivityAddNewContentBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()
        val NotesDao = (application as TaaveezApp).db.TaaveezDao()
        AddContent(NotesDao)
        binding?.isComplete?.setOnClickListener{
            openDialogForStatus()
        }

    }



    private fun openDialogForStatus() {
        val BackDialog = Dialog(this)
        BackDialog.setCancelable(false)
        val binding = DialogBinding.inflate(layoutInflater)
        BackDialog.setContentView(binding.root)

        binding?.dialogImage?.setImageResource(R.drawable.iv_setting)
        binding?.tvDialogHeading?.text = "Select Status for Content"
        binding?.btnBackNo?.text = "Complete"
        binding?.btnBackYes?.text = "Panding"


        binding?.btnBackNo?.setOnClickListener {
            isContentCompleteStatus = true
            BackDialog.dismiss()

        }
        binding?.btnBackYes?.setOnClickListener {
            isContentCompleteStatus = false
            BackDialog.dismiss()
        }
        BackDialog.show()
    }

    private fun takePhotoFromCamera() {
        Toast.makeText(this@Add_New_Content,"Choose photo from camera",Toast.LENGTH_LONG).show()
    }

    private fun choosePhotoFromGallery() {
//        requestStoragePermission()
        Toast.makeText(this@Add_New_Content,"Choose photo from gallery",Toast.LENGTH_LONG).show()

    }






    override fun onBackPressed() {
        // Call your desired method here
       BackData()
    }
    @SuppressLint("ResourceType")

     fun AddContent(TaaveezDao: TaaveezDao) {

        // bacground color change of richeditor
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.editor_bg, typedValue, true)
        val backgroundColor = typedValue.data

        // text color acc. to theme in richeditor
        theme.resolveAttribute(R.attr.text, typedValue, true)
        val textColor = typedValue.data

        val emptyText=""
        val Content_Description : RichEditor? = findViewById(R.id.idnotes)
        Content_Description?.setPlaceholder(getString(R.string.write_here))
        Content_Description?.setEditorBackgroundColor(backgroundColor)
        Content_Description?.setEditorFontColor(textColor)
        Content_Description?.setEditorFontSize(20)
        Content_Description?.html = emptyText    // this is used only for isEmpty not running correctly
        Content_Description?.setPadding(10, 10, 10, 10)
        Content_Description?.isVerticalScrollBarEnabled = true
        Content_Description?.setTextColor(Color.WHITE)
        binding?.btnBold?.setOnClickListener { Content_Description?.setBold() }
        binding?. btnItalic?.setOnClickListener { Content_Description?.setItalic() }
        binding?. btnUnderline?.setOnClickListener { Content_Description?.setUnderline()}
        binding?.btnUndo?.setOnClickListener {
            Content_Description?.undo()
        }


        binding?.btnRedo?.setOnClickListener {
            Content_Description?.redo()
        }



        binding?.btnAddLink?.setOnClickListener{
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_insert_link, null)
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Insert Link")
                    .setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        val urlEditText = dialogView.findViewById<EditText>(R.id.url_edit_text)
                        val titleEditText = dialogView.findViewById<EditText>(R.id.title_edit_text)
                        val url = urlEditText.text.toString()
                        val title = titleEditText.text.toString()
                        Content_Description?.insertLink(url, title)
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .create()
                dialog.show()
        }




        val maxLength = 21
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        binding?.idTopic?.filters = filterArray

        // Add a TextWatcher to the TextInputEditText view
        binding?.idTopic?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the length of the text is equal to the maximum length
                if (s?.length == maxLength) {
                    // Show a toast message
                    val message = "Maximum length of $maxLength characters exceeded"
                    Snackbar.make(binding?.idTopic!!, message, Snackbar.LENGTH_LONG).show()
                }
            }
        })






//        dd MMM yyyy HH:mm:ss
        binding?.saveContent?.setOnClickListener {
            var Content_Topic: String = binding?.idTopic?.text.toString()
            val html_Content_Description= Content_Description?.html.toString()
             var smalldes :String?=null
            // setup the date

            val c = Calendar.getInstance()
            val dateTime = c.time
            Log.e("Date: ", "" + dateTime)
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = sdf.format(dateTime)
            Log.e("Formatted Date: ", "" + date)


            if(html_Content_Description.toString().length <=25){
                smalldes = "$html_Content_Description..."
            }else{
                smalldes =  html_Content_Description.toString().substring(0,25)+ "..."
            }



            if (Content_Description?.html!!.isNotEmpty()) {
                if (!(TextUtils.isEmpty(Content_Topic.trim { it <= ' ' }))) {
                    lifecycleScope.launch {
                        TaaveezDao.insert(
                            TaaveezEntity(
                                Topic = Content_Topic,
                                Content = html_Content_Description,
                                Date = date,
                                CreatedDate = date,
                                SmallDes = smalldes,
                                isComplete = isContentCompleteStatus
                            )
                        )
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.Record_saved),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Content_Topic = "दुआ"
                    lifecycleScope.launch {
                        TaaveezDao.insert(
                            TaaveezEntity(
                                Topic = Content_Topic,
                                Content = html_Content_Description,
                                Date = date,
                                CreatedDate = date,
                                SmallDes = smalldes,
                                isComplete = isContentCompleteStatus
                            )
                        )
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.Record_saved),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                super.onBackPressed()
                overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_left)
            }else {
                val mess = getString(R.string.Field_not_blank)
                Snackbar.make(binding?.idTopic!!, mess, Snackbar.LENGTH_LONG).show()
            }

        }
    }


//    Handle the backspace button to undo the last action:
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && binding?.idnotes != null) {
            binding?.idnotes?.undo()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }



    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarAddNewContent)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.setTitle(0)
        }
        binding?.toolbarAddNewContent?.setNavigationOnClickListener { BackData() }
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

    private fun loadLocate(){
        val sharedPreferences=getSharedPreferences("Setting", Activity.MODE_PRIVATE)
        val language= sharedPreferences.getString("My_Lang","MyLang")
        if (language != null) {
            setLocate(language)
        }
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
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    private fun BackData() {
        val BackDialog = Dialog(this)
        BackDialog.setCancelable(false)
        val binding = DialogBinding.inflate(layoutInflater)
        BackDialog.setContentView(binding.root)
        binding?.btnBackNo?.setOnClickListener {
            BackDialog.dismiss()
        }
        binding?.btnBackYes?.setOnClickListener {
            BackDialog.dismiss()
            super.onBackPressed()
            overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_left)
        }
        BackDialog.show()
    }


}