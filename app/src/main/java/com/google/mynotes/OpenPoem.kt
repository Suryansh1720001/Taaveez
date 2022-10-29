package com.google.mynotes

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mynotes.databinding.ActivityNotesBinding
import com.google.mynotes.databinding.ActivityOpenPoemBinding
import com.google.mynotes.databinding.OpenNotesBinding
import kotlinx.coroutines.launch

class OpenPoem : AppCompatActivity() {

    private var binding :ActivityOpenPoemBinding ?=null
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
            finish()
        }







    }




}
