package com.itssuryansh.taaveez

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.itssuryansh.taaveez.databinding.ActivityEditContentBinding
import com.itssuryansh.taaveez.databinding.DialogBackAddNewContentBinding
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Edit_Content : AppCompatActivity() {
    private var binding: ActivityEditContentBinding? = null
    private lateinit var NotesDao: NotesDao

    var CreatedDate: String = ""

    private var id: Int? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocate()
        loadDayNight()
        id = intent.getIntExtra(Constants.ID, 0)
        super.onCreate(savedInstanceState)
        binding = ActivityEditContentBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()

        NotesDao = (application as NotesApp).db.NotesDao()

        // color acc to the theme - text and background color
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.editor_bg, typedValue, true)
        val backgroundColor = typedValue.data
        theme.resolveAttribute(R.attr.text, typedValue, true)
        val textColor = typedValue.data
        binding?.etUpdatePoem?.setTextColor(textColor)

        val PoemDes: RichEditor = findViewById(R.id.etUpdatePoem)
        PoemDes.setEditorFontSize(20)
        PoemDes?.setPlaceholder(getString(R.string.write_here))
        PoemDes?.setEditorBackgroundColor(backgroundColor)
        PoemDes?.setEditorFontColor(textColor)
        PoemDes.setPadding(10, 10, 10, 10)
        PoemDes.setVerticalScrollBarEnabled(true)

        val btnBold: ImageButton? = findViewById(R.id.btn_update_bold)
        btnBold?.setOnClickListener { PoemDes?.setBold() }
        val btnItalic: ImageButton? = findViewById(R.id.btn_update_italic)
        btnItalic?.setOnClickListener { PoemDes?.setItalic() }

        binding?.btnUdpateUndo?.setOnClickListener {
            PoemDes?.undo()
        }

        binding?.btnUpdateRedo?.setOnClickListener {
            PoemDes?.redo()
        }
        val btn_addLink = findViewById<ImageButton>(R.id.btn_update_addLink)
        btn_addLink.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_insert_link, null)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Insert Link")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    val urlEditText = dialogView.findViewById<EditText>(R.id.url_edit_text)
                    val titleEditText = dialogView.findViewById<EditText>(R.id.title_edit_text)
                    val url = urlEditText.text.toString()
                    val title = titleEditText.text.toString()
                    PoemDes.insertLink(url, title)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
            dialog.show()
        }
        val btnUnderline: ImageButton? = findViewById(R.id.btn_update_underline)
        btnUnderline?.setOnClickListener { PoemDes?.setUnderline() }

        // setUP the content in the editview and richeditor from the Database
        lifecycleScope.launch {
            NotesDao.fetchNotesById(id!!).collect {
                if (it != null) {
                    binding?.etPoemTopic?.setText(it.Topic)
                    binding?.etUpdatePoem?.setHtml(it.Poem)
                    CreatedDate = it.CreatedDate
                }
            }
        }

        // edit text  - topic lenght size limit
        val maxLength = 21
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        binding?.etPoemTopic?.filters = filterArray

        // Add a TextWatcher to the TextInputEditText view
        binding?.etPoemTopic?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the length of the text is equal to the maximum length
                if (s?.length!! == maxLength) {
                    // Show a toast message
                    val message = "Maximum length of $maxLength characters exceeded"
                    Snackbar.make(binding?.etPoemTopic!!, message, Snackbar.LENGTH_LONG).show()
//                    Toast.makeText(this@Edit_Content,message, Toast.LENGTH_LONG).show()
                }
            }
        })

        binding?.btnUpdatePoem?.setOnClickListener {
            updateData()
        }
    }

    //    Handle the backspace button to undo the last action:
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && binding?.etUpdatePoem != null) {
            binding?.etUpdatePoem?.undo()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun updateData() {
        var Topic = binding?.etPoemTopic?.text.toString()
        var Poem = binding?.etUpdatePoem?.html

        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date: ", "" + dateTime)
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted Date: ", "" + date)

        if (!(Poem!!.isEmpty())) {
            if (!(TextUtils.isEmpty(Topic.trim { it <= ' ' }))) {
                lifecycleScope.launch {
                    NotesDao.update(NotesEntity(id!!, Topic, Poem, date, CreatedDate))
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG)
                        .show()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            } else {
                Topic = "दुआ"
                lifecycleScope.launch {
                    NotesDao.update(NotesEntity(id!!, Topic, Poem, date, CreatedDate))
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG)
                        .show()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            super.onBackPressed()
        } else {
            val mess = getString(R.string.Field_not_blank)
            Snackbar.make(binding?.etUpdatePoem!!, mess, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Setting", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "MyLang")
        if (language != null) {
            setLocate(language)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarUpdateContent)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.setTitle(0)
        }
        binding?.toolbarUpdateContent?.setNavigationOnClickListener { BackData() }
    }

    private fun BackData() {
        val BackDialog = Dialog(this)
        BackDialog.setCancelable(false)
        val binding = DialogBackAddNewContentBinding.inflate(layoutInflater)
        BackDialog.setContentView(binding.root)
        binding?.btnBackNo?.setOnClickListener {
            BackDialog.dismiss()
        }
        binding?.btnBackYes?.setOnClickListener {
            BackDialog.dismiss()
            super.onBackPressed()
        }
        BackDialog.show()
    }

    override fun onBackPressed() {
        // Call your desired method here
        BackData()
    }
}
