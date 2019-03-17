package com.example.posts.presentation.utils

import android.widget.ImageView

fun ImageView.loadAvatar(email: String) =
    loadImageRound("https://api.adorable.io/avatars/285/$email")
