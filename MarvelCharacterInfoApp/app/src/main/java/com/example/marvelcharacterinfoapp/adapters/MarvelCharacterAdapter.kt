package com.example.marvelcharacterinfoapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.marvelcharacterinfoapp.R
import com.example.marvelcharacterinfoapp.models.MarvelCharacter

class MarvelCharacterAdapter(private val context: Context, private val dataSource: MutableList<MarvelCharacter>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.character_item, parent, false)

            holder = ViewHolder()
            holder.nameTextView = view.findViewById(R.id.character_name) as TextView
            holder.thumbnailImageView = view.findViewById(R.id.character_thumbnail) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val name = holder.nameTextView
        val thumbnail = holder.thumbnailImageView

        name.text = dataSource[position].name
        Log.d("URL", dataSource[position].thumbnailUrl)
        thumbnail.setImageBitmap(dataSource[position].getThumbnail(context))
        return view
    }

    private class ViewHolder {
        lateinit var nameTextView: TextView
        lateinit var thumbnailImageView: ImageView
    }

}