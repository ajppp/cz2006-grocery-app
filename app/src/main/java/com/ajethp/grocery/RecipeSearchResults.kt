package com.ajethp.grocery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeSearchResults : AppCompatActivity() {

    companion object {
        private const val TAG = "RecipeSearchResults"
    }

    private lateinit var searchResultTitleText: TextView
    private lateinit var searchResultRvBoard: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_search_results)

        val recipeId = intent.getIntegerArrayListExtra("ID")
        val recipeName = intent.getStringArrayListExtra("NAME")
        recipeName!!.toMutableSet()

        searchResultTitleText = findViewById(R.id.searchResultTitleText)
        searchResultRvBoard = findViewById(R.id.searchResultRvBoard)

        "you searched for '${intent.getStringExtra("QUERY")}'".also { searchResultTitleText.text = it }

        searchResultRvBoard.adapter = SearchResultAdapter(this, recipeName) {
            val clickedRecipeId: String = recipeId!![it].toString()
            Log.i(TAG, clickedRecipeId)
            val intent = Intent(this, RecipeDetails::class.java)
            intent.putExtra("RECIPE_ID", clickedRecipeId)
            intent.putExtra("RECIPE_NAME", recipeName[it])
            startActivity(intent)
        }
        searchResultRvBoard.setHasFixedSize(true)
        // spanCount is the number of columns
        searchResultRvBoard.layoutManager = LinearLayoutManager(this)
    }

}