package com.example.marvelcharacterinfoapp.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import java.net.URL

object ThumbnailHelper {
    fun create(resources: Resources, urlLink: String) : Bitmap {
        val url = URL(urlLink)
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        val round = RoundedBitmapDrawableFactory.create(resources, bmp)

        round.isCircular = true
        round.setAntiAlias(true)
        return round.toBitmap()
    }
}