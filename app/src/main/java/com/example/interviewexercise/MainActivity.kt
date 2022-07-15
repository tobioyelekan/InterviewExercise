package com.example.interviewexercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.interviewexercise.views.gallery.GalleryFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, GalleryFragment.newInstance())
            .commitNow()
    }
}
