package com.ajethp.grocery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class RecipeDetails : AppCompatActivity() {

    companion object {
        private const val TAG = "RecipeDetails"
    }

    private lateinit var recipeTextName: TextView
    private lateinit var recipeIngredientsRvBoard: RecyclerView
    private lateinit var recipeDetailsRvBoard: RecyclerView
    private lateinit var recipeDoneButton: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val recipeId = intent.getStringExtra("RECIPE_ID")
        val recipeName = intent.getStringExtra("RECIPE_NAME")
        // get the recipe instructions
        val instructionUrl = "https://api.spoonacular.com/recipes/$recipeId/analyzedInstructions?apiKey=fbb942433c0d4382a68fef26d5554e5f"
        val ingredientUrl = "https://api.spoonacular.com/recipes/$recipeId/ingredientWidget.json?apiKey=fbb942433c0d4382a68fef26d5554e5f"

        recipeTextName = findViewById(R.id.recipeTitle)
        recipeDetailsRvBoard = findViewById(R.id.recipeDetails)
        recipeIngredientsRvBoard = findViewById(R.id.recipeIngredientRvBoard)
        recipeDoneButton = findViewById(R.id.recipeDoneButton)
        val recipeSteps: MutableList<String> = arrayListOf()
        val recipeIngredients: MutableList<String> = arrayListOf()

        Thread {
            // get recipe instructions and parse
            val responseText = client.newCall(Request.Builder().url(instructionUrl).get().build()).execute().body()!!.string()
            val responseArray = JSONArray(responseText)
            val size = responseArray.length()
            for (i in 0 until size) {
                val jsonObject = responseArray.getJSONObject(i)
                val stepsJSONArray = JSONArray(jsonObject.optString("steps").toString())
                val stepsSize = stepsJSONArray.length()
                for (j in 0 until stepsSize) {
                    val stepObject = stepsJSONArray.getJSONObject(j)
                    val recipeStep = stepObject.optString("step").toString()
                    recipeSteps.add(recipeStep)
                }
            }
            //get recipe ingredients and parse
            val ingredientResponseText = client.newCall(Request.Builder().url(ingredientUrl).get().build()).execute().body()!!.string()
            Log.i(TAG, ingredientResponseText)
            val ingredientArray = JSONArray(JSONObject(ingredientResponseText).optString("ingredients"))
            val numIngredients = ingredientArray.length()
            for (i in 0 until numIngredients) {
                val ingredientObject = ingredientArray.getJSONObject(i)
                val ingredientName = ingredientObject.optString("name")
                val ingredientQuantity = JSONObject(JSONObject(ingredientObject.optString("amount")).optString("metric")).optString("value") +
                        JSONObject(JSONObject(ingredientObject.optString("amount")).optString("metric")).optString("unit")
                val ingredientText = "$ingredientName: $ingredientQuantity"
                recipeIngredients.add(ingredientText)
                // if (recipeIngredients.size == 0) {recipeIngredients.add(ingredientText)}
                // else if (recipeIngredients.size > 0) {recipeIngredients[0].plus("\n $ingredientText")}
            }

            runOnUiThread {
                recipeTextName.text = recipeName

                recipeIngredientsRvBoard.adapter = RecipeIngredientsAdapter(this, recipeIngredients)
                recipeIngredientsRvBoard.setHasFixedSize(true)
                recipeIngredientsRvBoard.layoutManager = LinearLayoutManager(this)

                recipeDetailsRvBoard.adapter = RecipeDetailsAdapter(this, recipeSteps)
                recipeDetailsRvBoard.setHasFixedSize(true)
                recipeDetailsRvBoard.layoutManager = GridLayoutManager(this, 1)

                recipeDoneButton.setOnClickListener {
                    //TODO("when the user clicks on the done button, need to do something?")
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }.start()
    }
}