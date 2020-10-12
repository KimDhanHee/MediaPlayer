package com.example.mediaplayer.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mediaplayer.R

@BindingAdapter("app:imageUri")
fun imageUri(iv: ImageView, uri: String?) =
  Glide.with(iv)
    .load(Uri.parse(uri ?: ""))
    .centerCrop()
    .placeholder(R.drawable.placeholder)