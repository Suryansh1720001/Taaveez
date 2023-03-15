package com.itssuryansh.taaveez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.Toast
import com.google.taaveez.R
import com.google.taaveez.databinding.ActivityOpenPoemBinding
import kotlinx.android.synthetic.main.activity_open_poem.*
import javax.sql.StatementEvent


class OpenPoem : AppCompatActivity() {

    private var binding :ActivityOpenPoemBinding?=null
    private var PoemTopic: String? =null
    private var PoemDes: String? =null
    private var CreatedDate :String?=null
    private var UpdatedDate :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        PoemTopic = intent.getStringExtra(Constants.POEM_TOPIC)
        PoemDes = intent.getStringExtra(Constants.POEM_DES)
        CreatedDate = intent.getStringExtra(Constants.CREATED_DATE)
        UpdatedDate = intent.getStringExtra(Constants.UPDATED_DATE)


        super.onCreate(savedInstanceState)
        binding = ActivityOpenPoemBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvTopic?.setText(PoemTopic)
        binding?.tvPoemDes?.text = Html.fromHtml(PoemDes)
        tvPoemDes?.movementMethod = LinkMovementMethod.getInstance() // make links clickable
//        binding?.tvPoemDes?.setText(PoemDes)


        binding?.tvPoemCreatedDate?.setText("Created Date : " +CreatedDate)
        binding?.tvPoemUpdatedDate?.setText("Updated Date : " +UpdatedDate)

        binding?.btnClose?.setOnClickListener {
            openNotesActivity()
        }
    }

    override fun onBackPressed() {
        openNotesActivity()
    }

    fun openNotesActivity(){
        val intent = Intent(this@OpenPoem, Notes::class.java)
        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(R.drawable.slide_in_left, R.drawable.slide_out_rigth)
        finish()
    }


}
