package com.example.marvelcharacterinfoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.marvelcharacterinfoapp.adapters.MarvelCharacterAdapter
import com.example.marvelcharacterinfoapp.models.MarvelCharacter
import com.example.marvelcharacterinfoapp.providers.CharacterProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    var page: Int = 1
    var dataResult = mutableListOf<MarvelCharacter>()
    val characterProvider: CharacterProvider = CharacterProvider()
    lateinit var charAdapter: MarvelCharacterAdapter
    val pagesButtons = mutableListOf<Button>()
    var pageButtonSelected = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val lvResults = findViewById<ListView>(R.id.lvHeroes)
        charAdapter = MarvelCharacterAdapter(this, dataResult)
        lvResults.adapter = charAdapter
        lvResults.onItemClickListener = this

        previous_page.setOnClickListener { updateCharacterList(page - 1) }
        previous_page.visibility = View.INVISIBLE
        next_page.setOnClickListener { updateCharacterList(page + 1) }

        pagesButtons.add(findViewById(R.id.page_button1))
        pagesButtons.add(findViewById(R.id.page_button2))
        pagesButtons.add(findViewById(R.id.page_button3))

        for ((index, value) in pagesButtons.withIndex()) {
            value.setOnClickListener { onPageButtonClicked(index + 1) }
        }


        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KEYCODE_ENTER) {
                    et_search.clearFocus()
                    val inputManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(et_search.windowToken, 0)

                    updateCharacterList(1)
                    return true
                }

                return false
            }
        })

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onStart() {
        super.onStart()

        updateCharacterList(page)
    }

    //OnItemClickListener
    override fun onItemClick(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Log.d("SELECIONADO", dataResult[pos]?.name)
        val intent = Intent(this, CharacterActivity::class.java)
        intent.putExtra("Character", dataResult[pos])
        startActivity(intent)
    }

    private fun onPageButtonClicked(index: Int) {
        if (pageButtonSelected == index)
            return

        updateCharacterList(page + (index - pageButtonSelected))
    }

    private fun updateCharacterList(newPage: Int) {
        pageButtonSelected = 1
        if (newPage <= 1) {
            page = 1
        } else if (newPage >= characterProvider.totalPages) {
            page = characterProvider.totalPages
            pageButtonSelected = 3
        } else {
            page = newPage
            pageButtonSelected = 2
        }

        updateSearch()
        updatePageButtons(pageButtonSelected)
    }

    private fun updatePageButtons(selected: Int) {
        for ((index, value) in pagesButtons.withIndex()) {
            if (selected - 1 == index) {
                value.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.pagination_button_selected,
                    null
                )
                value.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.colorSecondary,
                        null
                    )
                )
                value.text = page.toString()
            } else {
                value.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.pagination_button, null)
                value.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                value.text = (page + (index - (selected - 1))).toString()
            }
        }

        previous_page.visibility = if (selected == 1) View.INVISIBLE else View.VISIBLE
        next_page.visibility =
            if (selected == pagesButtons.count()) View.INVISIBLE else View.VISIBLE
    }

    private fun updateSearch() {
        val characters = characterProvider.getCharacters(page, et_search.text.toString())
        dataResult.clear()
        dataResult.addAll(characters)
        charAdapter.notifyDataSetChanged()

        pagination_container.visibility =
            if (characterProvider.totalPages < 2) View.INVISIBLE else View.VISIBLE

    }
}
