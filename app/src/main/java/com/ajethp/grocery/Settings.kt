package com.ajethp.grocery

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.gson.Gson

class Settings : AppCompatActivity() {

    private val restrictionsList = arrayOf(
            "beef",
            "pork",
            "etc",
            "idk what else there is",
            "some random crap",
            "whatever"
    )

    companion object {
        private const val TAG = "Settings"
    }

    private lateinit var currentUser: User
    private lateinit var userSharedPreferences: SharedPreferences

    private lateinit var updateRestrictionsButton: Button
    private lateinit var languageButton: Button
    private lateinit var fontSizeButton: Button
    private lateinit var familyButton: Button
    private lateinit var settingsUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)

        updateRestrictionsButton = findViewById(R.id.updateRestrictionsButton)
        languageButton = findViewById(R.id.languageButton)
        fontSizeButton = findViewById(R.id.fontSizeButton)
        familyButton = findViewById(R.id.familyButton)
        settingsUserName = findViewById(R.id.settingsUsername)
        settingsUserName.text = currentUser.username

        updateRestrictionsButton.setOnClickListener { showDietaryRestrictionAlertDialog("Dietary Restrictions", null) {} }
    }

    private fun showDietaryRestrictionAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setMultiChoiceItems(restrictionsList, currentUser.dietaryRestriction.toBooleanArray()) { _, which, isChecked -> currentUser.dietaryRestriction[which] = isChecked }
                .setPositiveButton("DONE") { _, _ -> }
                .show()
    }

    override fun onStop() {
        super.onStop()
        DataBaseHelper(this).modifyUserRestriction(currentUser)
        startActivity(Intent(this, MainActivity::class.java))
    }
}