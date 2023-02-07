package com.google.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.mynotes.databinding.ActivityOpenPoemBinding


class OpenPoem : AppCompatActivity() {

    private var binding :ActivityOpenPoemBinding?=null
    private var PoemTopic: String? =null
    private var PoemDes: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        PoemTopic = intent.getStringExtra(Constants.POEM_TOPIC)
        PoemDes = intent.getStringExtra(Constants.POEM_DES)

        super.onCreate(savedInstanceState)
        binding = ActivityOpenPoemBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvTopic?.setText(PoemTopic)
        binding?.tvPoemDes?.setText(PoemDes)

        binding?.btnClose?.setOnClickListener {

            val intent = Intent(this@OpenPoem,Notes::class.java)
            intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            overridePendingTransition(R.drawable.slide_in_left,R.drawable.slide_out_rigth);

        }
    }
}
