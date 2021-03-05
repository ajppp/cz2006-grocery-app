package com.ajethp.grocery

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class Recipe : AppCompatActivity() {
    companion object{
         private const val TAG = "Recipe"
    }

    private lateinit var currentUser: User
    private lateinit var userSharedPreferences: SharedPreferences

    private lateinit var recipeRvBoard: RecyclerView

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {

        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)
        val userJsonString = userSharedPreferences.getString("USER", "")
        Log.i(TAG, userJsonString!!)
        currentUser = Gson().fromJson(userJsonString, User::class.java)

        var _url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients="
        val firstIngredient = currentUser.inventoryList[0].foodName
        val secondIngredient = currentUser.inventoryList[1].foodName
        val thirdIngredient = currentUser.inventoryList[2].foodName
        var ingredients = "$firstIngredient,+$secondIngredient,+$thirdIngredient"
        val numberOfRecipes = "&number=3&"
        val apiKey = "apiKey=fbb942433c0d4382a68fef26d5554e5f"

        val url = _url+ingredients+numberOfRecipes+apiKey

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        recipeRvBoard = findViewById(R.id.recipeRvBoard)

        Thread {
            val responseText = client.newCall(Request.Builder().url(url).get().build()).execute().body()!!.string()
            var responseArray  = JSONArray(responseText)
            var size = responseArray.length()
            var suggestedRecipe: MutableList<String> = arrayListOf()
            for (i in 0 until size) {
                val responseObject = responseArray.getJSONObject(i)
                val recipeName = responseObject.optString("title").toString()
                suggestedRecipe.add(recipeName)
                Log.i(TAG, responseObject.toString())
                Log.i(TAG, recipeName)
            }

            runOnUiThread {
                recipeRvBoard.adapter = RecipeAdapter(this, suggestedRecipe)
                recipeRvBoard.setHasFixedSize(true)
                // spanCount is the number of columns
                recipeRvBoard.layoutManager = GridLayoutManager(this, 1)
            }
        }.start()
        }
    }

    // private fun getRecommendedRecipe() {
        // val request = Request.Builder()
            // .url("https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=2&apiKey=fbb942433c0d4382a68fef26d5554e5f")
            // .get()
            // .build()

        // val response = client.newCall(request).execute()

// headerNameIterator.forEach {
            // Log.i(TAG, it)
        // }
    //}
// }