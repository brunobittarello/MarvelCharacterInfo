package com.example.marvelcharacterinfoapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.marvelcharacterinfoapp.models.MarvelCharacter
import com.example.marvelcharacterinfoapp.models.MarvelCharacterData
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.content_character.*

class CharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        setSupportActionBar(toolbar)

        val character = intent.getSerializableExtra("Character") as MarvelCharacter

        val view = layoutInflater.inflate(R.layout.character_item, null)
        view.findViewById<TextView>(R.id.character_name).text = character.name
        view.findViewById<ImageView>(R.id.character_thumbnail).setImageBitmap(character.getThumbnail(this))
        character_form.addView(view)

        title = character.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showCharacterData(character.comics, this.resources.getString(R.string.comics))
        showCharacterData(character.series, this.resources.getString(R.string.series))
        showCharacterData(character.stories, this.resources.getString(R.string.stories))
        showCharacterData(character.events, this.resources.getString(R.string.events))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showCharacterData(data: MarvelCharacterData, dataName: String) {
        if (data.items.count() == 0)
            return

        var view = layoutInflater.inflate(R.layout.character_data_label, null)
        view.findViewById<TextView>(R.id.character_data_item_label).text = dataName
        character_form.addView(view)

        for (item in data.items) {
            view = layoutInflater.inflate(R.layout.character_data_item, null)
            view.findViewById<TextView>(R.id.character_data_item_text).text = item.name
            character_form.addView(view)
        }

    }

}
