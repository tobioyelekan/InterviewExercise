package com.example.interviewexercise.views

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

fun View.showMessage(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String) {
    if (url.isNotEmpty()) {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}