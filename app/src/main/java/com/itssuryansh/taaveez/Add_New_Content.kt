package com.itssuryansh.taaveez

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.itssuryansh.taaveez.databinding.ActivityAddNewContentBinding
import com.itssuryansh.taaveez.databinding.DialogBackAddNewContentBinding
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Add_New_Content : AppCompatActivity() {

    private var binding: ActivityAddNewContentBinding? = null
    private val IMAGE_PICKER_REQUEST_CODE = 1001 // or any other unique value

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocate()
        loadDayNight()
        super.onCreate(savedInstanceState)

        binding = ActivityAddNewContentBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()
        val NotesDao = (application as NotesApp).db.NotesDao()
        AddContent(NotesDao)
    }

    override fun onBackPressed() {
        // Call your desired method here
        BackData()
    }

    @SuppressLint("ResourceType")
    fun AddContent(NotesDao: NotesDao) {
        // bacground color change of richeditor
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.editor_bg, typedValue, true)
        val backgroundColor = typedValue.data

        // text color acc. to theme in richeditor
        theme.resolveAttribute(R.attr.text, typedValue, true)
        val textColor = typedValue.data

        val PoemDes: RichEditor? = findViewById(R.id.idnotes)
        PoemDes?.setPlaceholder(getString(R.string.write_here))
        PoemDes?.setEditorBackgroundColor(backgroundColor)
        PoemDes?.setEditorFontColor(textColor)
        PoemDes?.setEditorFontSize(20)
        PoemDes?.setPadding(10, 10, 10, 10)
        PoemDes?.isVerticalScrollBarEnabled = true
        PoemDes?.setTextColor(Color.WHITE)

        binding?.btnBold?.setOnClickListener { PoemDes?.setBold() }
        binding?. btnItalic?.setOnClickListener { PoemDes?.setItalic() }
        binding?. btnUnderline?.setOnClickListener { PoemDes?.setUnderline() }
        binding?.btnUndo?.setOnClickListener {
            PoemDes?.undo()
        }

        binding?.btnRedo?.setOnClickListener {
            PoemDes?.redo()
        }

        binding?.btnAddLink?.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_insert_link, null)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Insert Link")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    val urlEditText = dialogView.findViewById<EditText>(R.id.url_edit_text)
                    val titleEditText = dialogView.findViewById<EditText>(R.id.title_edit_text)
                    val url = urlEditText.text.toString()
                    val title = titleEditText.text.toString()
                    PoemDes?.insertLink(url, title)
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

        binding?.saveContent?.setOnClickListener {
            var itemTopic: String = binding?.idTopic?.text.toString()
            val htmlContentPoemDes = PoemDes?.html.toString()
            // setup the date
            val c = Calendar.getInstance()
            val dateTime = c.time
            Log.e("Date: ", "" + dateTime)
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
            val date = sdf.format(dateTime)
            Log.e("Formatted Date: ", "" + date)

            if (PoemDes?.html!!.isNotEmpty()) {
                if (!(TextUtils.isEmpty(itemTopic.trim { it <= ' ' }))) {
                    lifecycleScope.launch {
                        NotesDao.insert(NotesEntity(Topic = itemTopic, Poem = htmlContentPoemDes, Date = date, CreatedDate = date))
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.Record_saved),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                } else {
                    itemTopic = "दुआ"
                    lifecycleScope.launch {
                        NotesDao.insert(
                            NotesEntity(
                                Topic = itemTopic,
                                Poem = htmlContentPoemDes,
                                Date = date,
                                CreatedDate = date,
                            ),
                        )
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.Record_saved),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }
                super.onBackPressed()
            } else {
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
}
