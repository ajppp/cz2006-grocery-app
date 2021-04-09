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

/**
 * This class is an activity class which
 * implements the Settings activity. It is the
 * settings page of our app
 *
 * @author jethro
 * @author claudia
 */
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

    private lateinit var updateRestrictionsButton: Button
    private lateinit var languageButton: Button
    private lateinit var fontSizeButton: Button
    private lateinit var familyButton: Button
    private lateinit var createFamilyButton: Button
    private lateinit var settingsUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)

        updateRestrictionsButton = findViewById(R.id.updateRestrictionsButton)
        languageButton = findViewById(R.id.languageButton)
        fontSizeButton = findViewById(R.id.fontSizeButton)
        familyButton = findViewById(R.id.familyButton)
        createFamilyButton = findViewById(R.id.createFamilyButton)
        settingsUserName = findViewById(R.id.settingsUsername)
        settingsUserName.text = currentUser.username

        // update restrictions
        updateRestrictionsButton.setOnClickListener { showDietaryRestrictionAlertDialog("Dietary Restrictions", null) {} }

        if (currentUser.familyId == 0) {
            familyButton.text = "Create or Join Family"
            // join family
            familyButton.setOnClickListener {
                startActivity(Intent(this, JoinFamilyActivity::class.java))
            }

            createFamilyButton.setOnClickListener {
                startActivity(Intent(this, CreateFamily::class.java))
            }
        } else {
            createFamilyButton.visibility = View.GONE
            familyButton.text = "View Family Information"
            familyButton.setOnClickListener {
                startActivity(Intent(this, ViewFamily::class.java))
            }
        }
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