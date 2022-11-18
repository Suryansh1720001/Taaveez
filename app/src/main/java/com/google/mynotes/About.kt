package com.google.mynotes

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.mynotes.databinding.ActivityAboutBinding

class About : AppCompatActivity() {
    private var binding:ActivityAboutBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.tvAbout?.setText(getString(R.string.about_poem))

        binding?.btnAboutBack?.setOnClickListener {
            finish()
            overridePendingTransition(R.drawable.slide_in_right,R.drawable.slide_out_rigth);

        }


    }
}