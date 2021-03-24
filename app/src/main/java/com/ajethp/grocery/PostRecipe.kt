package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.helper.DataBaseHelper

class PostRecipe : AppCompatActivity() {

    private lateinit var postRecipeRvBoard: RecyclerView
    private lateinit var postRecipeDoneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_recipe)

        postRecipeRvBoard = findViewById(R.id.postRecipeRvBoard)
        postRecipeDoneButton = findViewById(R.id.postRecipeDoneButton)

        val recipeIngredients = intent.getStringArrayListExtra("INGREDIENTS")
        val currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)
        val usedRecipeInInventory = ArrayList<String>()

        /**
         * check through the items that are used for the recipe
         * and compare them with those that the user has in
         * their inventory. Only display those that are in the
         * user's inventory
         */

        for (ingredients in currentUser.inventoryList) {
            // TODO("hi cloud, you can suggest ideas on how we want to do the string matching")
            for (recipeIngredient in recipeIngredients!!) {
                if (recipeIngredient.contains(ingredients.foodName, ignoreCase = true)) {
                    usedRecipeInInventory.add(recipeIngredient)
                }
            }
        }

        postRecipeRvBoard.adapter = PostRecipeAdapter(this,usedRecipeInInventory)
        postRecipeRvBoard.setHasFixedSize(true)
        postRecipeRvBoard.layoutManager = LinearLayoutManager(this)

        postRecipeDoneButton.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}