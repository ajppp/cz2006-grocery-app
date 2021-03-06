package com.ajethp.grocery

import android.content.Context
import android.content.Intent
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

        // gets the top three ingredients in users inventory list and uses it to query the system
        val firstIngredient = currentUser.inventoryList[0].foodName
        val secondIngredient = currentUser.inventoryList[1].foodName
        val thirdIngredient = currentUser.inventoryList[2].foodName
        val url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=$firstIngredient,+$secondIngredient,+$thirdIngredient&number=3&ranking=1&ignorePantry=true&apiKey=fbb942433c0d4382a68fef26d5554e5f"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        recipeRvBoard = findViewById(R.id.recipeRvBoard)
        val suggestedRecipeName: MutableList<String> = arrayListOf()
        val suggestedRecipeId: MutableList<String> = arrayListOf()

        Thread {
            val responseText = client.newCall(Request.Builder().url(url).get().build()).execute().body()!!.string()
            val responseArray  = JSONArray(responseText)
            val size = responseArray.length()
            for (i in 0 until size) {
                val responseObject = responseArray.getJSONObject(i)
                val recipeId = responseObject.optString("id").toString()
                val recipeName = responseObject.optString("title").toString()
                suggestedRecipeName.add(recipeName)
                suggestedRecipeId.add(recipeId)
            }


            runOnUiThread {
                recipeRvBoard.adapter = RecipeAdapter(this, suggestedRecipeName) {
                    val clickedRecipeId: String = suggestedRecipeId[it]
                    val intent = Intent(this, RecipeDetails::class.java)
                    intent.putExtra("RECIPE_ID", clickedRecipeId)
                    intent.putExtra("RECIPE_NAME", suggestedRecipeName[it])
                    startActivity(intent)
                }
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