package com.itssuryansh.taaveez.activity

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.itssuryansh.taaveez.Constants
import com.itssuryansh.taaveez.R
import com.itssuryansh.taaveez.databinding.ActivityOpenContentBinding
import com.itssuryansh.taaveez.databinding.DialogAboutOfOpenContentBinding


class Open_Content : AppCompatActivity() {

    private var binding : ActivityOpenContentBinding?=null
    private var Content_Topic: String? =null
    private var Content_Description: String? =null
    private var CreatedDate :String?=null
    private var UpdatedDate :String?=null
    private var textToCopy : String? = null
    private var id :Int? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        loadDayNight()
        Content_Topic = intent.getStringExtra(Constants.TAAVEEZ_CONTENT_TOPIC)
        Content_Description = intent.getStringExtra(Constants.TAAVEEZ_CONTENT_DESCRIPTION)
        CreatedDate = intent.getStringExtra(Constants.CREATED_DATE)
        UpdatedDate = intent.getStringExtra(Constants.UPDATED_DATE)
        id = intent.getIntExtra(Constants.ID,0)




        super.onCreate(savedInstanceState)

        binding = ActivityOpenContentBinding.inflate(layoutInflater)
        setContentView(binding?.root)

     binding?.btnMenu?.setOnClickListener{
        popup()

     }

        setupActionBar()

        binding?.tvTopic?.text = Content_Topic


        binding?.tvPoemDes?.text = Html.fromHtml(Content_Description)

        binding?.tvPoemDes?.movementMethod = LinkMovementMethod.getInstance() // enable link clicking
        binding?.tvPoemDes?.setTextIsSelectable(true) // enable text selection

        textToCopy = Html.fromHtml(Content_Description).toString()
    }



    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarOpenContent)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.setTitle(0)
        }
        binding?.toolbarOpenContent?.setNavigationOnClickListener { openNotesActivity() }
    }

    private fun popup() {
        val popupMenu = PopupMenu(this, binding?.btnMenu)
        popupMenu.inflate(R.menu.menu)


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
                    true
                }

                R.id.copy_data -> {
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("label", textToCopy)
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
        val sendIntent = Intent()
        sendIntent.type = "text/plain"
        sendIntent.action = Intent.ACTION_SEND
        val body = "Topic = ${Content_Topic}\n" +
                "--------------------------------\n" +
                "${Html.fromHtml(Content_Description)}"
        sendIntent.putExtra(Intent.EXTRA_TEXT, body)
        Intent.createChooser(sendIntent, "Share using")
        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(sendIntent)
    }

    private fun dialog_about_open_content() {
        val AboutDialog = Dialog(this)
        AboutDialog.setCancelable(false)
        val binding = DialogAboutOfOpenContentBinding.inflate(layoutInflater)
        AboutDialog.setContentView(binding.root)


        binding?.tvPoemCreatedDate?.text = CreatedDate
        binding?.tvPoemUpdatedDate?.text = UpdatedDate


        binding?.btnAboutOpenContentBack?.setOnClickListener {
            AboutDialog.dismiss()
        }
        AboutDialog.show()
    }


    override fun onBackPressed() {
        openNotesActivity()
    }

    fun openNotesActivity(){
        val intent = Intent(this@Open_Content, HomePage::class.java)
        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_rigth)
        finish()
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




}
