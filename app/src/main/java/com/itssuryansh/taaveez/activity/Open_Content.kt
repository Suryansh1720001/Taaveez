package com.itssuryansh.taaveez.activity

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.itssuryansh.taaveez.helper.Constants
import com.itssuryansh.taaveez.R
import com.itssuryansh.taaveez.TaaveezApp
import com.itssuryansh.taaveez.databinding.ActivityOpenContentBinding
import com.itssuryansh.taaveez.databinding.DialogAboutOfOpenContentBinding
import kotlinx.coroutines.launch
import java.util.*


class Open_Content : AppCompatActivity() {

    private var binding : ActivityOpenContentBinding?=null
//    private var Content_Topic: String? =null
//    private var Content_Description: String? =null
//    private var textToCopy : String? = null
    private var id :Int? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        loadDayNight()
        loadLocate()
        id = intent.getIntExtra(Constants.ID,0 )




        super.onCreate(savedInstanceState)

        // Set the status bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.status_bar_color)
        }

        binding = ActivityOpenContentBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val TaaveezDao = (application as TaaveezApp).db.TaaveezDao()
        lifecycleScope.launch {
            TaaveezDao.fetchContentsById(id!!).collect {
                if (it != null) {
                    binding?.tvTopic?.text =  it.Topic
                    binding?.tvContentDescription?.text = Html.fromHtml(it.Content)
                }
            }

        }



        binding?.btnMenu?.setOnClickListener{
        popup()

     }

        setupActionBar()
//
//     Content_Topic =    binding?.tvTopic?.text.toString()
//      Content_Description =    binding?.tvContentDescription?.text.toString()
          binding?.tvContentDescription?.movementMethod = LinkMovementMethod.getInstance() // enable link clicking
//         binding?.tvContentDescription?.setTextIsSelectable(true) // enable text selection
//        textToCopy = binding?.tvContentDescription?.text.toString()
    }



    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarOpenContent)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.setTitle(0)
        }
        binding?.toolbarOpenContent?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun popup() {
        val popupMenu = PopupMenu(this, binding?.btnMenu)
        popupMenu.inflate(R.menu.menu)

        val copy_text = "Topic = ${ binding?.tvTopic?.text.toString()}\n" +
                "--------------------------------\n" +
                "${binding?.tvContentDescription?.text}"


//// Set the background color of the PopupMenu
//        val popupMenuHelper = MenuPopupHelper(this, popupMenu.menu as MenuBuilder, binding?.btnMenu)
//        popupMenuHelper.setGravity(Gravity.BOTTOM)
//        popupMenuHelper.setBgColor(ContextCompat.getColor(this, R.color.popup_menu_background))
//        popupMenuHelper.show()



        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.edit_data -> {
                    val intent = Intent(this@Open_Content, Edit_Content::class.java)
                    intent.putExtra(Constants.ID,id)
                    startActivity(intent)
                    overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_left);
                    true
                }

                R.id.copy_data -> {
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("label", copy_text)
                    clipboard.setPrimaryClip(clip)
                    true

                }
                R.id.share_data -> {
                   shareContent()
                    true

                }
                R.id.about_data -> {
                    // Handle about data action
                    dialog_about_open_content()
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



    private fun shareContent() {
        var body = "Topic = ${ binding?.tvTopic?.text.toString()}\n" +
                "--------------------------------\n" +
                "${Html.fromHtml(binding?.tvContentDescription?.text.toString())}"

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






    private fun dialog_about_open_content() {
        val AboutDialog = Dialog(this)
        AboutDialog.setCancelable(true)
        val binding = DialogAboutOfOpenContentBinding.inflate(layoutInflater)
        AboutDialog.setContentView(binding.root)

//        if(isContentCompleteStatus==true){
//            binding?.ivIsContentComplete?.setImageResource(R.drawable.ic_complete)
//        }else{
//            binding?.ivIsContentComplete?.setImageResource(R.drawable.ic_incomplete)
//        }

        val TaaveezDao = (application as TaaveezApp).db.TaaveezDao()
        lifecycleScope.launch {
            TaaveezDao.fetchContentsById(id!!).collect {
                if (it != null) {
                    binding?.tvPoemCreatedDate?.text = it.CreatedDate
                    binding?.tvPoemUpdatedDate?.text = it.Date // updated date
//                    isContentCompleteStatus = it.isComplete

                    if(it.isComplete){
                        binding?.ivIsContentComplete?.setImageResource(R.drawable.ic_complete)
                    }else{
                        binding?.ivIsContentComplete?.setImageResource(R.drawable.ic_incomplete)
                    }
                }
            }

        }



//        binding?.btnAboutOpenContentBack?.setOnClickListener {
//            AboutDialog.dismiss()
//        }
        AboutDialog.show()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_rigth)
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
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }

    private fun loadLocate(){
        val sharedPreferences=getSharedPreferences("Setting", Activity.MODE_PRIVATE)
        val language= sharedPreferences.getString("My_Lang","MyLang")
        if (language != null) {
            setLocate(language)
        }
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






}
