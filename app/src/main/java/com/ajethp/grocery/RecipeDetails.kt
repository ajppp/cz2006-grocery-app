package com.ajethp.grocery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class RecipeDetails : AppCompatActivity() {

    companion object {
        private const val TAG = "RecipeDetails"
    }

    private lateinit var recipeDetailsRvBoard: RecyclerView
    private lateinit var recipeDoneButton: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val recipeId = intent.getStringExtra("RECIPE_ID").toString()

        // get the recipe instructions
        val url = "https://api.spoonacular.com/recipes/$recipeId/analyzedInstructions?apiKey=fbb942433c0d4382a68fef26d5554e5f"

        recipeDetailsRvBoard = findViewById(R.id.recipeDetails)
        recipeDoneButton = findViewById(R.id.recipeDoneButton)
        val recipeSteps: MutableList<String> = arrayListOf()

        Thread {
            val responseText = client.newCall(Request.Builder().url(url).get().build()).execute().body()!!.string()
            val responseArray = JSONArray(responseText)
            val size = responseArray.length()
            for (i in 0 until size) {
                val jsonObject = responseArray.getJSONObject(i)
                val stepsJSONArray = JSONArray(jsonObject.optString("steps").toString())
                val stepsSize = stepsJSONArray.length()
                for (i in 0 until stepsSize) {
                    val stepObject = stepsJSONArray.getJSONObject(i)
                    val recipeStep = stepObject.optString("step").toString()
                    recipeSteps.add(recipeStep)
                }
            }

            runOnUiThread {
                recipeDetailsRvBoard.adapter = RecipeDetailsAdapter(this, recipeSteps)
                recipeDetailsRvBoard.setHasFixedSize(true)
                recipeDetailsRvBoard.layoutManager = GridLayoutManager(this, 1)

                recipeDoneButton.setOnClickListener {
                    //TODO("when the user clicks on the done button, need to do something")
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }.start()
    }
}