package com.ajethp.grocery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeSearchResults : AppCompatActivity() {

    private lateinit var searchResultTitleText: TextView
    private lateinit var searchResultRvBoard: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_search_results)

        val recipeId = intent.getIntegerArrayListExtra("ID")
        val recipeName = intent.getStringArrayListExtra("NAME")

        searchResultTitleText = findViewById(R.id.searchResultTitleText)
        searchResultRvBoard = findViewById(R.id.searchResultRvBoard)

        "you searched for '${intent.getStringExtra("QUERY")}'".also { searchResultTitleText.text = it }

        searchResultRvBoard.adapter = SearchResultAdapter(this, recipeName)
        searchResultRvBoard.setHasFixedSize(true)
        searchResultRvBoard.layoutManager = LinearLayoutManager(this)
    }

}