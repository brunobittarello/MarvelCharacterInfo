package com.example.marvelcharacterinfoapp.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.example.marvelcharacterinfoapp.utils.ThumbnailHelper
import java.io.ByteArrayOutputStream
import java.io.Serializable

class MarvelCharacter(
    val id: Int,
    val name: String,
    val thumbnail: Thumbnail,
    val comics: MarvelCharacterData,
    val series: MarvelCharacterData,
    val stories: MarvelCharacterData,
    val events: MarvelCharacterData
) : Serializable {

    val thumbnailUrl: String
        get() = thumbnail.path + "." + thumbnail.extension

    private val thumbnailFile: String
        get() = "$id.png"

    fun getThumbnail(context: Context) : Bitmap {
        val file = context.getFileStreamPath(thumbnailFile)
        var draw = Drawable.createFromPath(file.toString())
        if (draw != null)
            return  draw.toBitmap()

        val bitmap = ThumbnailHelper.create(context.resources, thumbnailUrl)
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val stream = context.openFileOutput(thumbnailFile, MODE_PRIVATE)
        stream.write(baos.toByteArray())
        stream.close()
        return bitmap
    }
}

class Thumbnail(val path: String, val extension: String) : Serializable

class MarvelCharacterData(val items: List<MarvelCharacterDataDataItem>) : Serializable

class MarvelCharacterDataDataItem(val resourceURI: String, val name: String) : Serializable