package com.itssuryansh.taaveez.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.*
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.itssuryansh.taaveez.*
import com.itssuryansh.taaveez.adapter.itemAdapter
import com.itssuryansh.taaveez.database.TaaveezDao
import com.itssuryansh.taaveez.databinding.*
import com.itssuryansh.taaveez.helper.Constants
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.activity_add_new_content.*
import kotlinx.android.synthetic.main.menu_header.tv_navigationBar_heading
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class HomePage : AppCompatActivity() {

    private var binding: ActivityHomePageBinding? = null
    private var Content_Description_Update: RichEditor ?=null



    private var isDetailedViewMode = false // Initialize the view mode to false (normal view)

// drawable
lateinit var drawerlayout : DrawerLayout
    lateinit var navigationView : NavigationView
    lateinit var toolbar : Toolbar

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {

        loadLocate()
        loadDayNight()
        super.onCreate(savedInstanceState)
        binding =ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val typeface: Typeface = Typeface.createFromAsset(  assets,"museo.ttf")

        binding?.tvNotesHeading?.typeface = typeface




        // Retrieve the boolean value from SharedPreferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val savedBooleanValue = prefs.getBoolean(Constants.IS_DETAILED_VIEW, false) // "false" is the default value if the key is not found
        isDetailedViewMode = savedBooleanValue


        // Set the status bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = getColor(R.color.status_bar_color)
            }
        }


        // drawable
        drawerlayout = findViewById(R.id.menu_drawer)
        navigationView = findViewById(R.id.navigation_menu)
        toolbar = findViewById(R.id.menuButton)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu)
            actionBar.setTitle(0)
        }

        val toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_open, R.string.navigation_close)

//        drawerlayout.addDrawerListener(toggle)
//        toggle.isDrawerIndicatorEnabled = true
//        toggle.syncState()


        navigationView = findViewById<View>(R.id.navigation_menu) as NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.chooseLanguage -> showChangeLang()
                R.id.switchTheme -> showChangeTheme()
                R.id.share ->  share()
                R.id.feedback -> feedback()
                R.id.report_bug -> ReportBug()
                R.id.source_code -> sourceCode()
                R.id.open_source_library -> openSourceLibrary()
                R.id.PrivacyPolicy -> PrivacyPolicy()
                R.id.about_us -> About()
                R.id.appVersionOption -> AppVersionOption()

            }
            closeDrawer()
            false
        }



//        binding?.tvabout?.setOnClickListener {
//            val intent = Intent(this@HomePage, About::class.java )
//            startActivity(intent)
//            overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_rigth);
//            finish()
//
//        }
//        binding?.tvSetting?.setOnClickListener {
//            val intent = Intent(this@HomePage, Setting::class.java)
//            startActivity(intent)
//                overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_left);
//            finish()
//
//        }


        binding?.btnHomePageMenu?.setOnClickListener{
            popup()
        }

        val TaaveezDao = (application as TaaveezApp).db.TaaveezDao()
        binding?.idFABAdd?.setOnClickListener {
            val intent = Intent(this@HomePage, Add_New_Content::class.java)
            startActivity(intent)
            overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_rigth)
        }


        lifecycleScope.launch {
            TaaveezDao.fetchAllContents().collect {
                val list = ArrayList(it)
                setupListOfDateINtoRecycleVIew(list, TaaveezDao, isDetailedViewMode)
            }
        }



    }







    // This function can be called when the user interacts with your app, like when clicking a button.
    private fun saveBooleanValue(value: Boolean) {
        val editor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean(Constants.IS_DETAILED_VIEW.toString(), value)
        editor.apply() // Use apply() to save the changes asynchronously, or use commit() for synchronous save.
    }


    private fun closeDrawer() {
        val drawer = findViewById<DrawerLayout>(R.id.menu_drawer)
        drawer.closeDrawer(GravityCompat.START)
    }



    // ALL OPTIONS FOR NAVIGATION BAR


    private fun showChangeLang() {
        val ChooseLangaugeDialog = Dialog(this)
        val binding = DialogBinding.inflate(layoutInflater)
        ChooseLangaugeDialog.setContentView(binding.root)

        binding?.dialogImage?.setImageResource(R.drawable.choose_language_pic)
        binding?.tvDialogHeading?.text =  getString(R.string.choose_langauge)
        binding?.btnBackNo?.text = getString(R.string.english)
        binding?.btnBackYes?.text = getString(R.string.hindi)

        binding?.btnBackNo?.setOnClickListener {
            setLocate("en")
            recreate()
            ChooseLangaugeDialog.dismiss()

        }
        binding?.btnBackYes?.setOnClickListener {
            setLocate("hi")
            recreate()
            ChooseLangaugeDialog.dismiss()
        }

        ChooseLangaugeDialog.show()
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



    // load Dark night after open the app
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun loadDayNight(){
        val sharedPreferences=getSharedPreferences("DayNight", MODE_PRIVATE)
        val DayNight= sharedPreferences.getString("My_DayNight","MyDayNight")
        if (DayNight != null) {
            setDayNight(DayNight)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setDayNight(daynightMode: String) {
        val editor = getSharedPreferences("DayNight", MODE_PRIVATE).edit()
        editor.putString("My_DayNight",daynightMode)
        editor.apply()

        if(daynightMode=="yes"){

            // set for dark mode
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showChangeTheme() {
        val ChangeThemeDialog = Dialog(this)
        val binding = DialogBinding.inflate(layoutInflater)
        ChangeThemeDialog.setContentView(binding.root)

        binding.dialogImage?.setImageResource(R.drawable.theme_pic)
        binding.tvDialogHeading?.text = getString(R.string.select_theme)
        binding.btnBackNo?.text = getString(R.string.dark)
        binding.btnBackYes?.text = getString(R.string.light)


        binding?.btnBackNo?.setOnClickListener {


            setDayNight("yes")
            Constants.Theme = "1"
            ChangeThemeDialog.dismiss()





        }
        binding?.btnBackYes?.setOnClickListener {



            setDayNight("no")
            Constants.Theme = "0"
            ChangeThemeDialog.dismiss()

        }
        ChangeThemeDialog.show()
    }




    // FOR BUTTON SHARE
    private fun share() {
        val i : ImageView = ImageView(applicationContext)
        i.setImageResource(R.drawable.taaveez_image)
        val bitmapDrawable = i.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val uri: Uri = getImageToShare(bitmap)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("image/*")
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_subject))
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



    // For FEEDBACK button
    private fun feedback() {
        Toast.makeText(this@HomePage ,getString(R.string.scroll_down_for_rating), Toast.LENGTH_LONG).show()
        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()))
        startActivity(playStoreIntent)
    }



     // For REPORT BUG
     private fun ReportBug() {
         val recipient = getString(R.string.email)
         val subject = getString(R.string.subject_for_email)
         val message = getString(R.string.email_body)

         val intent = Intent(Intent.ACTION_SENDTO).apply {
             data = Uri.parse("mailto:")
             putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
             putExtra(Intent.EXTRA_SUBJECT, subject)
             putExtra(Intent.EXTRA_TEXT, message)
         }
         startActivity(intent)
     }


    // For Source Code Button

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



    // For Open-Source Library Button
    private fun openSourceLibrary(){
        val link = "https://taaveez.vercel.app/open-source/taaveez-open-source.html"
        val intent = Intent(this@HomePage, WebView::class.java)
        intent.putExtra(Constants.LINK, link)
        startActivity(intent)
    }

    // For Privacy Policy Button
    private fun PrivacyPolicy(){
        val link = "https://taaveez.vercel.app/privacy-policy/taaveez-privacy-policy.html"
        val intent = Intent(this@HomePage, WebView::class.java)
        intent.putExtra(Constants.LINK, link)
        startActivity(intent)
    }


    //For ABout Button

    private fun About(){
        val intent = Intent(this@HomePage, About::class.java )
        startActivity(intent)
        overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_rigth);
    }


// For APplication version

    private fun AppVersionOption(){


        Toast.makeText(this@HomePage,getString(R.string.About_version) + getString(R.string.appVersion),Toast.LENGTH_LONG).show()
    }









    @SuppressLint("NotifyDataSetChanged")
    private fun popup() {
        val popupMenu = PopupMenu(this, binding?.btnHomePageMenu)
        popupMenu.inflate(R.menu.home_page_menu)
        val TaaveezDao = (application as TaaveezApp).db.TaaveezDao()

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.normal_view -> {
                   Toast.makeText(this@HomePage,getString(R.string.normal_view_set),Toast.LENGTH_LONG).show()
                    isDetailedViewMode = false
                    saveBooleanValue(isDetailedViewMode)

                    lifecycleScope.launch {
                        TaaveezDao.fetchAllContents().collect {
                            val list = ArrayList(it)
                            setupListOfDateINtoRecycleVIew(list, TaaveezDao, isDetailedViewMode)
                        }
                    }


                    true
                }

                R.id.detailed_view -> {
                    Toast.makeText(this@HomePage,getString(R.string.detailed_view_set),Toast.LENGTH_LONG).show()
                    isDetailedViewMode = true
                    saveBooleanValue(isDetailedViewMode)
                    lifecycleScope.launch {
                        TaaveezDao.fetchAllContents().collect {
                            val list = ArrayList(it)
                            setupListOfDateINtoRecycleVIew(list, TaaveezDao, isDetailedViewMode)
                        }
                    }

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






//
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun NewPoemDialog(TaaveezDao: TaaveezDao) {
//
//        val PoemDialog = Dialog(this)
//        PoemDialog.setCancelable(false)
//        PoemDialog.setContentView(R.layout.notes_add_dialog)
//
//        val PoemDes :RichEditor = PoemDialog.findViewById(R.id.idnotes)
//        PoemDes.setPlaceholder(getString(R.string.write_here))
//        PoemDes.setEditorHeight(200)
//        PoemDes.setEditorFontSize(22)
//        PoemDes.setPadding(10, 10, 10, 10)
//        PoemDes.setVerticalScrollBarEnabled(true)
//        PoemDes.setTextColor(Color.WHITE);
//
//        val btnBold : ImageButton?= PoemDialog.findViewById(R.id.btn_bold)
//        btnBold?.setOnClickListener { PoemDes?.setBold() }
//        val btnItalic : ImageButton?= PoemDialog.findViewById(R.id.btn_italic)
//        btnItalic?.setOnClickListener { PoemDes?.setItalic() }
//        val btnUnderline : ImageButton? = PoemDialog.findViewById(R.id.btn_underline)
//        btnUnderline?.setOnClickListener { PoemDes?.setUnderline() }
//
//        val cancelBtn = PoemDialog.findViewById<Button>(R.id.idBtnCancel)
//        val addBtn = PoemDialog.findViewById<Button>(R.id.idBtnAdd)
//        val itemTopic = PoemDialog.findViewById<EditText>(R.id.idTopic)
//        val btn_addLink = PoemDialog.findViewById<ImageButton>(R.id.btn_add_link)
//
//
//        btn_addLink.setOnClickListener{
//                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_insert_link, null)
//                val dialog = AlertDialog.Builder(this)
//                    .setTitle("Insert Link")
//                    .setView(dialogView)
//                    .setPositiveButton("OK") { _, _ ->
//                        val urlEditText = dialogView.findViewById<EditText>(R.id.url_edit_text)
//                        val titleEditText = dialogView.findViewById<EditText>(R.id.title_edit_text)
//                        val url = urlEditText.text.toString()
//                        val title = titleEditText.text.toString()
//                        PoemDes.insertLink(url, title)
//                    }
//                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
//                    .create()
//                dialog.show()
//        }
//
//        cancelBtn.setOnClickListener {
//            PoemDialog.dismiss()
//        }
//
//        addBtn.setOnClickListener {
//            var itemTopic: String = itemTopic.text.toString()
//            val htmlContentPoemDes= PoemDes.html.toString()
//
//            // setup the date
//            val c = Calendar.getInstance()
//            val dateTime = c.time
////            Log.e("Date: ", "" + dateTime)
//            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
//            val date = sdf.format(dateTime)
////            Log.e("Formatted Date: ", "" + date)
//
//            if (!(PoemDes.html.isNullOrEmpty())) {
//                if (!(TextUtils.isEmpty(itemTopic.trim { it <= ' ' }))) {
//                    lifecycleScope.launch {
//                        TaaveezDao.insert(TaaveezEntity(Topic = itemTopic, Content = htmlContentPoemDes, Date = date, CreatedDate = date))
//                        Toast.makeText(applicationContext,
//                            getString(R.string.Record_saved),
//                            Toast.LENGTH_LONG).show()
//                    }
//
//                } else {
//                    itemTopic = "दुआ"
//                    lifecycleScope.launch {
//                        TaaveezDao.insert(
//                            TaaveezEntity(Topic = itemTopic, Content = htmlContentPoemDes, Date = date,
//                            CreatedDate = date )
//                        )
//                        Toast.makeText(applicationContext,
//                            getString(R.string.Record_saved),
//                            Toast.LENGTH_LONG).show()
//
//                    }
//
//                }
//                PoemDialog.dismiss()
//                Toast.makeText(this, "$htmlContentPoemDes", Toast.LENGTH_LONG).show()
//
//            } else {
//                Toast.makeText(this, getString(R.string.field_cannot_be_blank), Toast.LENGTH_LONG).show()
//            }
//
//        }
//        PoemDialog.show()
//    }
//



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

                 mdetailedViewMode = detailedViewMode


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
        var Topic: String? = "Topic"
        var Content_Description: String? = "Content_Description"
        var body:String?="Body"

        lifecycleScope.launch {
            TaaveezDao.fetchContentsById(id).collect {
                if (it != null) {
                    Topic = it.Topic
                    Content_Description = it.Content

                }
            }

        }


        body = "Topic = ${Topic}\n" +
                "--------------------------------\n" +
                "${Html.fromHtml(Content_Description)}"

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, body)
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.share_title)
            )
        )




    }







    private fun openContent(id: Int, TaaveezDao: TaaveezDao) {

//
//        var Content_Topic :String? = null
//        var Content_Description :String?  = "Content_Topic"
//        var CreatedDate :String? = "Content_Topic"
//        var UpdatedDate : String? = "Content_Topic"
//        var isContentCompleteStatus : Boolean? = false

//        lifecycleScope.launch {
//            TaaveezDao.fetchContentsById(id).collect {
//                if (it != null) {
//                    Content_Topic = it.Topic
//                    Content_Description = it.Content
//                    CreatedDate = it.CreatedDate
//                    UpdatedDate = it.Date
//                    isContentCompleteStatus = it.isComplete
//
//
//                }
//            }
//        }

        val intent = Intent(this@HomePage, Open_Content::class.java)
//        intent.putExtra(Constants.TAAVEEZ_CONTENT_TOPIC, Content_Topic)
//        intent.putExtra(Constants.TAAVEEZ_CONTENT_DESCRIPTION,Content_Description)
//        intent.putExtra(Constants.CREATED_DATE,CreatedDate)
//        intent.putExtra(Constants.UPDATED_DATE,UpdatedDate)
//        intent.putExtra(Constants.IS_CONTENT_COMPLETE_STATUS,isContentCompleteStatus)
        intent.putExtra(Constants.ID,id)
        startActivity(intent)
//                    overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_rigth);

        overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_left)

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
        var isContentCompleteStatus : Boolean?=false

        lifecycleScope.launch {
            TaaveezDao.fetchContentsById(id).collect {
                if (it != null) {
                    binding.etPoemTopic.setText(it.Topic)
                    binding.etUpdatePoem.setHtml(it.Content)
                    CreatedDate = it.CreatedDate
                    isContentCompleteStatus = it.isComplete
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
            var Content_description = binding.etUpdatePoem.html

            var smalldes :String?=null

            if(Content_description.toString().length <=25){
                smalldes = "$Content_description..."
            }else{
                smalldes =  Content_description.toString().substring(0,25)+ "..."
            }






            val c = Calendar.getInstance()
            val dateTime = c.time
            Log.e("Date: ", "" + dateTime)
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = sdf.format(dateTime)
            Log.e("Formatted Date: ", "" + date)


            if (!(Content_description.isEmpty())) {
                if (!(TextUtils.isEmpty(Topic.trim { it <= ' ' }))) {
                    lifecycleScope.launch {
                        TaaveezDao.update(TaaveezEntity(id, Topic, Content_description, date,CreatedDate,smalldes,isContentCompleteStatus!!))
                        Toast.makeText(applicationContext, getString(R.string.record_updated), Toast.LENGTH_LONG)
                            .show()
                        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        updateDialog.dismiss()
                    }
                } else {
                    Topic = "दुआ"
                    lifecycleScope.launch {
                        TaaveezDao.update(TaaveezEntity(id, Topic, Content_description, date, CreatedDate,smalldes,isContentCompleteStatus!!))
                        Toast.makeText(applicationContext, getString(R.string.record_updated), Toast.LENGTH_LONG)
                            .show()
                        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        updateDialog.dismiss()
                    }
                }
            } else{
                    Toast.makeText(applicationContext, getString(R.string.field_cannot_be_blank), Toast.LENGTH_LONG)
                        .show()
                }
            }

            binding.btnCancelPoem.setOnClickListener {
                updateDialog.dismiss()
            }
            updateDialog.show()
        }







    private fun deleteRecordAlertDialog(id: Int, employeeDao: TaaveezDao) {
        val deleteDialog = Dialog(this)
        deleteDialog.setCancelable(false)
        val binding = DialogBinding.inflate(layoutInflater)
        deleteDialog.setContentView(binding.root)

        binding?.dialogImage?.setImageResource(R.drawable.iv_image_2)
        binding?.tvDialogHeading?.text = getString(R.string.delete_content)


        binding?.btnBackNo?.setOnClickListener {
            deleteDialog.dismiss()

        }
        binding?.btnBackYes?.setOnClickListener {
            lifecycleScope.launch {
                employeeDao.delete(TaaveezEntity(id))
            }
            deleteDialog.dismiss()

        }
        deleteDialog.show()
    }

    override fun onBackPressed() {
        BackButton()
    }


    private fun BackButton() {
        val BackDialog = Dialog(this)
        BackDialog.setCancelable(false)
        val binding = DialogBinding.inflate(layoutInflater)
        BackDialog.setContentView(binding.root)

        binding?.dialogImage?.setImageResource(R.drawable.iv_image_1)
        binding?.tvDialogHeading?.text = "Want to stay more with Taaveez?"


        binding?.btnBackNo?.setOnClickListener {
            BackDialog.dismiss()
            super.onBackPressed()

        }
        binding?.btnBackYes?.setOnClickListener {
            BackDialog.dismiss()
        }
        BackDialog.show()
    }






}










