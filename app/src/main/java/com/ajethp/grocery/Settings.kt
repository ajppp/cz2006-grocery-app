package com.ajethp.grocery

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class Settings : AppCompatActivity() {

    // should be attributes of User
    val restrictionsList = arrayOf(
            "beef",
            "pork",
            "etc",
            "idk what else there is",
            "some random crap",
            "whatever"
    )

    var checkedRestrictions = booleanArrayOf(
            false,
            false,
            false,
            false,
            false,
            false
    )

    companion object {
        private const val TAG = "Settings"
    }

    private lateinit var updateRestrictionsButton: Button
    private lateinit var languageButton: Button
    private lateinit var fontSizeButton: Button
    private lateinit var familyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        updateRestrictionsButton = findViewById(R.id.updateRestrictionsButton)
        languageButton = findViewById(R.id.languageButton)
        fontSizeButton = findViewById(R.id.fontSizeButton)
        familyButton = findViewById(R.id.familyButton)

        updateRestrictionsButton.setOnClickListener({
            showDietaryRestrictionAlertDialog("Dietary Restrictions", null, View.OnClickListener {
            })
        })
    }

    // NOT DONE
    // HOW TO GET IT TO SAVE THE THINGS THAT HAVE BEEN MODIFIED
    private fun showDietaryRestrictionAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setMultiChoiceItems(restrictionsList, checkedRestrictions) { dialog, which, isChecked ->
                    checkedRestrictions[which] = !checkedRestrictions[which]
                    val currentItem = restrictionsList[which]
                    Log.i(TAG, "pressing $currentItem")
                }
                .setPositiveButton("DONE") { dialog, which ->
                }
                .show()
    }
}