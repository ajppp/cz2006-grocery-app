package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_recipe.*
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
    private lateinit var recipeSuggestionText: TextView
    private lateinit var recipeSearchBar: SearchView

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)
        currentUser.inventoryList.sortBy { it.expiryDate }

        recipeRvBoard = findViewById(R.id.recipeRvBoard)
        recipeSearchBar = findViewById(R.id.recipeSearchBar)
        val suggestedRecipeName: MutableList<String> = arrayListOf()
        val suggestedRecipeId: MutableList<String> = arrayListOf()


        // gets the top three ingredients in users inventory list and uses it to query the system
        if (currentUser.inventoryList.size > 2) {
            val firstIngredient = currentUser.inventoryList[0].foodName
            val secondIngredient = currentUser.inventoryList[1].foodName
            val thirdIngredient = currentUser.inventoryList[2].foodName
            val url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=$firstIngredient,+$secondIngredient,+$thirdIngredient&number=3&ranking=1&ignorePantry=true&apiKey=fbb942433c0d4382a68fef26d5554e5f"

            Thread {
                recipeSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String) : Boolean {
                        val queryUrl = "https://api.spoonacular.com/recipes/complexSearch?query=$query&number=10&apiKey=fbb942433c0d4382a68fef26d5554e5f"
                        Log.i(TAG, query)
                        val queryResponseText = client.newCall(Request.Builder().url(queryUrl).get().build()).execute().body()!!.string()
                        Log.i(TAG, queryResponseText)
                        return false
                    }

                    /**
                     * Called when the query text is changed by the user.
                     *
                     * @param newText the new content of the query text field.
                     *
                     * @return false if the SearchView should perform the default action of showing any
                     * suggestions if available, true if the action was handled by the listener.
                     */
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                        TODO("Not yet implemented")
                    }
                })
            }.start()

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
        } else {
            recipeRvBoard.visibility = View.INVISIBLE
            recipeSuggestionText = findViewById(R.id.recipeSuggestionText)
            recipeSuggestionText.text = "You do not have enough ingredients for us to suggest recipes"
        }
    }
}