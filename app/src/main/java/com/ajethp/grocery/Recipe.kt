package com.ajethp.grocery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class Recipe : AppCompatActivity() {
    companion object{
        private const val TAG = "Recipe"
    }

    private lateinit var recipeRvBoard: RecyclerView

    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        recipeRvBoard = findViewById(R.id.recipeRvBoard)

        Thread { getRecommendedRecipe() }.start()

        // TODO("replace 3 with the actual number of suggested recipes")
        recipeRvBoard.adapter = RecipeAdapter(this, 3)
        recipeRvBoard.setHasFixedSize(true)
        // spanCount is the number of columns
        recipeRvBoard.layoutManager = GridLayoutManager(this, 1)
    }

    private fun getRecommendedRecipe() {
        val request = Request.Builder()
            .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?ingredients=apples%2Cflour%2Csugar&number=3&ranking=1&ignorePantry=true")
            .get()
            .addHeader("x-rapidapi-key", "bd07ba6061msh8837e8eb1488270p1e4586jsna6edf2899b9d")
            .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()

        val headerNameIterator = response.body()!!.string()
        Log.i(TAG, headerNameIterator)

        // headerNameIterator.forEach {
            // Log.i(TAG, it)
        // }
    }
}