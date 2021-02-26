package com.ajethp.grocery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Recipe : AppCompatActivity() {
    companion object{
        private const val TAG = "Inventory"
    }

    private lateinit var recipeRvBoard: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        recipeRvBoard = findViewById(R.id.recipeRvBoard)

        // TODO("replace 3 with the actual number of suggested recipes")
        recipeRvBoard.adapter = RecipeAdapter(this, 3)
        recipeRvBoard.setHasFixedSize(true)
        // spanCount is the number of columns
        recipeRvBoard.layoutManager = GridLayoutManager(this, 1)
    }
}