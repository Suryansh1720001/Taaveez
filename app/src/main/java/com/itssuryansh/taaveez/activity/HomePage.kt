package com.itssuryansh.taaveez.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.*
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.itssuryansh.taaveez.*
import com.itssuryansh.taaveez.adapter.itemAdapter
import com.itssuryansh.taaveez.databinding.ActivityHomePageBinding
import com.itssuryansh.taaveez.databinding.DeleteItemBinding
import com.itssuryansh.taaveez.databinding.UpdateContentBinding
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomePage : AppCompatActivity() {

    private var binding: ActivityHomePageBinding? = null
    private var Content_Description_Update: RichEditor ?=null

private var isDetailedViewMode = true // Initialize the view mode to false (normal view)



    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {

        loadLocate()
        loadDayNight()
        super.onCreate(savedInstanceState)
        binding =ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val typeface: Typeface = Typeface.createFromAsset(  assets,"arabian_onenighjtstand.ttf")

        binding?.tvNotesHeading?.typeface = typeface



        binding?.tvabout?.setOnClickListener {
            val intent = Intent(this@HomePage, About::class.java )
            startActivity(intent)
            overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_rigth);
            finish()

        }
        binding?.tvSetting?.setOnClickListener {
            val intent = Intent(this@HomePage, Setting::class.java)
            startActivity(intent)
                overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_left);
            finish()

        }

        binding?.btnHomePageMenu?.setOnClickListener{
            popup()
        }

        val TaaveezDao = (application as TaaveezApp).db.TaaveezDao()
        binding?.idFABAdd?.setOnClickListener {
//            NewPoemDialog(NotesDao)
            val intent = Intent(this@HomePage, Add_New_Content::class.java)
            startActivity(intent)
        }


        lifecycleScope.launch {
            TaaveezDao.fetchAllContents().collect {
                val list = ArrayList(it)
                setupListOfDateINtoRecycleVIew(list, TaaveezDao, isDetailedViewMode)
            }
        }



    }



    @SuppressLint("NotifyDataSetChanged")
    private fun popup() {
        val popupMenu = PopupMenu(this, binding?.btnHomePageMenu)
        popupMenu.inflate(R.menu.home_page_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.normal_view -> {
                   Toast.makeText(this@HomePage,"normal view",Toast.LENGTH_LONG).show()


                    isDetailedViewMode = false



                    true
                }

                R.id.detailed_view -> {
                    Toast.makeText(this@HomePage,"detailed view",Toast.LENGTH_LONG).show()
                    isDetailedViewMode = true
                    // Notify the adapter that the data has changed
//                    if (::itemAdapter.isInitialized) {
//                        itemAdapter.notifyDataSetChanged()
//                    }

                    true

                }
                else -> super.onOptionsItemSelected(item)
            }

        }

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception){
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }


    }







    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun NewPoemDialog(TaaveezDao: TaaveezDao) {

        val PoemDialog = Dialog(this)
        PoemDialog.setCancelable(false)
        PoemDialog.setContentView(R.layout.notes_add_dialog)

        val PoemDes :RichEditor = PoemDialog.findViewById(R.id.idnotes)
        PoemDes.setPlaceholder(getString(R.string.write_here))
        PoemDes.setEditorHeight(200)
        PoemDes.setEditorFontSize(22)
        PoemDes.setPadding(10, 10, 10, 10)
        PoemDes.setVerticalScrollBarEnabled(true)
        PoemDes.setTextColor(Color.WHITE);

        val btnBold : ImageButton?= PoemDialog.findViewById(R.id.btn_bold)
        btnBold?.setOnClickListener { PoemDes?.setBold() }
        val btnItalic : ImageButton?= PoemDialog.findViewById(R.id.btn_italic)
        btnItalic?.setOnClickListener { PoemDes?.setItalic() }
        val btnUnderline : ImageButton? = PoemDialog.findViewById(R.id.btn_underline)
        btnUnderline?.setOnClickListener { PoemDes?.setUnderline() }

        val cancelBtn = PoemDialog.findViewById<Button>(R.id.idBtnCancel)
        val addBtn = PoemDialog.findViewById<Button>(R.id.idBtnAdd)
        val itemTopic = PoemDialog.findViewById<EditText>(R.id.idTopic)
        val btn_addLink = PoemDialog.findViewById<ImageButton>(R.id.btn_add_link)


        btn_addLink.setOnClickListener{
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

        cancelBtn.setOnClickListener {
            PoemDialog.dismiss()
        }

        addBtn.setOnClickListener {
            var itemTopic: String = itemTopic.text.toString()
            val htmlContentPoemDes= PoemDes.html.toString()

            // setup the date
            val c = Calendar.getInstance()
            val dateTime = c.time
            Log.e("Date: ", "" + dateTime)
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
            val date = sdf.format(dateTime)
            Log.e("Formatted Date: ", "" + date)

            if (!(PoemDes.html.isNullOrEmpty())) {
                if (!(TextUtils.isEmpty(itemTopic.trim { it <= ' ' }))) {
                    lifecycleScope.launch {
                        TaaveezDao.insert(TaaveezEntity(Topic = itemTopic, Content = htmlContentPoemDes, Date = date, CreatedDate = date))
                        Toast.makeText(applicationContext,
                            getString(R.string.Record_saved),
                            Toast.LENGTH_LONG).show()
                    }

                } else {
                    itemTopic = "दुआ"
                    lifecycleScope.launch {
                        TaaveezDao.insert(
                            TaaveezEntity(Topic = itemTopic, Content = htmlContentPoemDes, Date = date,
                            CreatedDate = date )
                        )
                        Toast.makeText(applicationContext,
                            getString(R.string.Record_saved),
                            Toast.LENGTH_LONG).show()

                    }

                }
                PoemDialog.dismiss()
                Toast.makeText(this, "$htmlContentPoemDes", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "field cannot be blank", Toast.LENGTH_LONG).show()
            }

        }
        PoemDialog.show()
    }


    private fun setupListOfDateINtoRecycleVIew(
        NotesList: ArrayList<TaaveezEntity>,
        TaaveezDao: TaaveezDao,
        detailedViewMode:Boolean

    ) {
        if (NotesList.isNotEmpty()) {


            val itemAdapter = itemAdapter(
                    NotesList,
                    { updateId ->
                        updateRecordDialog(updateId, TaaveezDao)
                    },
                    { deleteId ->
                        deleteRecordAlertDialog(deleteId, TaaveezDao)
                    },
                    { OpenId ->
                        openContent(OpenId, TaaveezDao)
                    },
                    { ShareId ->
                        ShareContent(ShareId, TaaveezDao)
                    },

                        mdetailedViewMode = isDetailedViewMode


                )
                binding?.rvItemsPoem?.layoutManager = LinearLayoutManager(this)
                binding?.rvItemsPoem?.adapter = itemAdapter
                binding?.rvItemsPoem?.visibility = View.VISIBLE
                binding?.tvNoDataAvailable?.visibility = View.GONE
                binding?.ivNoData?.visibility = View.GONE
        } else {
            binding?.rvItemsPoem?.visibility = View.GONE
            binding?.tvNoDataAvailable?.visibility = View.VISIBLE
            binding?.ivNoData?.visibility = View.VISIBLE

        }
    }



    private fun ShareContent(id: Int, TaaveezDao: TaaveezDao) {

        var Topic: String?
        var PoemDes : String?

        lifecycleScope.launch {
            TaaveezDao.fetchContentsById(id).collect {
                if (it != null) {
                    Topic = it.Topic
                    PoemDes = it.Content
                    val sendIntent = Intent()
                    sendIntent.type = "text/plain"
                    sendIntent.action = Intent.ACTION_SEND
                    val body = "Topic = ${Topic}\n" +
                            "--------------------------------\n" +
                            "${Html.fromHtml(PoemDes)}"
                    sendIntent.putExtra(Intent.EXTRA_TEXT, body)
                    Intent.createChooser(sendIntent, "Share using")
                    intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(sendIntent)

                }
            }
        }
    }


    private fun openContent(id: Int, TaaveezDao: TaaveezDao) {
        var Content_Topic :String?
        var Content_Description :String?
        var CreatedDate :String?
        var UpdatedDate : String?

        lifecycleScope.launch {
            TaaveezDao.fetchContentsById(id).collect {
                if (it != null) {
                    Content_Topic = it.Topic
                    Content_Description = it.Content
                    CreatedDate = it.CreatedDate
                    UpdatedDate = it.Date



                    val intent = Intent(this@HomePage, Open_Content::class.java)
                    intent.putExtra(Constants.TAAVEEZ_CONTENT_TOPIC, Content_Topic)
                    intent.putExtra(Constants.TAAVEEZ_CONTENT_DESCRIPTION,Content_Description)
                    intent.putExtra(Constants.CREATED_DATE,CreatedDate)
                    intent.putExtra(Constants.UPDATED_DATE,UpdatedDate)
                    intent.putExtra(Constants.ID,id)
                    startActivity(intent)
                   
//                    overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_left);

                }
            }

        }


    }



    //    Handle the backspace button to undo the last action: in update dialog
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Content_Description_Update = findViewById(R.id.etUpdatePoem)
        if (keyCode == KeyEvent.KEYCODE_DEL && Content_Description_Update != null) {
            Content_Description_Update?.undo()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun updateRecordDialog(id: Int, TaaveezDao: TaaveezDao) {
        val updateDialog = Dialog(this)
        updateDialog.setCancelable(false)
        val binding = UpdateContentBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        // color acc to the theme - text and background color
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.editor_bg, typedValue, true)
        val backgroundColor = typedValue.data
        theme.resolveAttribute(R.attr.text, typedValue, true)
        val textColor = typedValue.data
        binding.etUpdatePoem?.setTextColor(textColor)


        val PoemDes: RichEditor = updateDialog.findViewById(R.id.etUpdatePoem)
        PoemDes.setEditorFontSize(20)
        PoemDes?.setEditorBackgroundColor(backgroundColor)
        PoemDes?.setEditorFontColor(textColor)
        PoemDes.setPadding(10, 10, 10, 10)
      PoemDes.setVerticalScrollBarEnabled(true);

        val btnBold : ImageButton? = updateDialog.findViewById(R.id.btn_update_bold)
        btnBold?.setOnClickListener { PoemDes?.setBold() }
        val btnItalic : ImageButton? = updateDialog.findViewById(R.id.btn_update_italic)
        btnItalic?.setOnClickListener { PoemDes?.setItalic() }
        val btn_addLink = updateDialog.findViewById<ImageButton>(R.id.btn_update_addLink)
        binding?.btnUdpateUndo?.setOnClickListener {
            PoemDes?.undo()
        }

        binding?.btnUpdateRedo?.setOnClickListener {
            PoemDes?.redo()
        }
        btn_addLink.setOnClickListener{
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
        val btnUnderline : ImageButton? = updateDialog.findViewById(R.id.btn_update_underline)
        btnUnderline?.setOnClickListener { PoemDes?.setUnderline() }



        var CreatedDate:String=""



        lifecycleScope.launch {
            TaaveezDao.fetchContentsById(id).collect {
                if (it != null) {
                    binding.etPoemTopic.setText(it.Topic)
                    binding.etUpdatePoem.setHtml(it.Content)
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
                if (s?.length == maxLength) {
                    // Show a toast message
                    val message = "Maximum length of $maxLength characters exceeded"
//                    Snackbar.make(binding?.etPoemTopic!!, message, Snackbar.LENGTH_LONG).show()
                    Toast.makeText(this@HomePage,message,Toast.LENGTH_LONG).show()
                }
            }
        })



        binding.btnUpdatePoem.setOnClickListener {
            var Topic = binding.etPoemTopic.text.toString()
            var Poem = binding.etUpdatePoem.html

            

            val c = Calendar.getInstance()
            val dateTime = c.time
            Log.e("Date: ", "" + dateTime)
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
            val date = sdf.format(dateTime)
            Log.e("Formatted Date: ", "" + date)


            if (!(Poem.isEmpty())) {
                if (!(TextUtils.isEmpty(Topic.trim { it <= ' ' }))) {
                    lifecycleScope.launch {
                        TaaveezDao.update(TaaveezEntity(id, Topic, Poem, date,CreatedDate))
                        Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG)
                            .show()
                        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        updateDialog.dismiss()
                    }
                } else {
                    Topic = "दुआ"
                    lifecycleScope.launch {
                        TaaveezDao.update(TaaveezEntity(id, Topic, Poem, date, CreatedDate))
                        Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG)
                            .show()
                        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        updateDialog.dismiss()
                    }
                }
            } else{
                    Toast.makeText(applicationContext, "filed cannot be blank", Toast.LENGTH_LONG)
                        .show()
                }
            }

            binding.btnCancelPoem.setOnClickListener {
                updateDialog.dismiss()
            }
            updateDialog.show()
        }


//    private fun Noraml_detailed_view(view : String) {
//
//
//
//        if(view.equals("Noraml_view")){
//
//        }
//        binding?.btnDeleteYes?.setOnClickListener {
//            lifecycleScope.launch {
//                employeeDao.delete(NotesEntity(id))
//                Toast.makeText(applicationContext,
//                    "Record deleted successfully",
//                    Toast.LENGTH_LONG).show()
//            }
//            deleteDialog.dismiss()
//
//        }
//
//        deleteDialog.show()
//    }




    private fun deleteRecordAlertDialog(id: Int, employeeDao: TaaveezDao) {

        val deleteDialog = Dialog(this)
        deleteDialog.setCancelable(false)
        val binding = DeleteItemBinding.inflate(layoutInflater)
        deleteDialog.setContentView(binding.root)

        binding?.btnDeleteNo?.setOnClickListener {
            deleteDialog.dismiss()
        }
        binding?.btnDeleteYes?.setOnClickListener {
            lifecycleScope.launch {
                employeeDao.delete(TaaveezEntity(id))
                    Toast.makeText(applicationContext,
                        "Record deleted successfully",
                        Toast.LENGTH_LONG).show()
                }
                deleteDialog.dismiss()

        }

        deleteDialog.show()
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
        val editor = getSharedPreferences("DayNight",Context.MODE_PRIVATE).edit()
        editor.putString("My_DayNight",daynightMode)
        editor.apply()
        if(daynightMode=="yes"){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }



}










