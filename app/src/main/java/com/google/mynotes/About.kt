package com.google.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.mynotes.databinding.ActivityAboutBinding

class About : AppCompatActivity() {
    private var binding:ActivityAboutBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.tvAbout?.setText("This is the Poem application in which you can store your application.")

        binding?.btnAboutBack?.setOnClickListener {
            finish()
        }


    }
}