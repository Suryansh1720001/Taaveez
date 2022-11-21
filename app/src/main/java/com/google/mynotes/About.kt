package com.google.mynotes

import android.content.Intent
import android.graphics.Typeface
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

        val typeface: Typeface =
            Typeface.createFromAsset(assets,"arabian_onenighjtstand.ttf")
        binding?.tvNotesHeading?.typeface = typeface

        binding?.tvAbout?.setText(getString(R.string.about_poem))

        binding?.btnAboutBack?.setOnClickListener {
            finish()
            overridePendingTransition(R.drawable.slide_in_right,R.drawable.slide_out_rigth);

        }


    }
}