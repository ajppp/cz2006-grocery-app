package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.android.material.snackbar.Snackbar
import java.math.BigInteger
import java.security.MessageDigest

class JoinFamilyActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "join family"
    }

    private lateinit var currentUser: User

    private lateinit var joinFamilyId: EditText
    private lateinit var joinFamilyPassword: EditText
    private lateinit var joinFamilyButton: Button
    private lateinit var joinFamilyClRoot: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_family)

        currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)

        joinFamilyId = findViewById(R.id.familyIdInput)
        joinFamilyPassword = findViewById(R.id.familyPasswordInput)
        joinFamilyButton = findViewById(R.id.joinFamilyButton)
        joinFamilyClRoot = findViewById(R.id.joinFamilyClRoot)

        joinFamilyButton.setOnClickListener {
            // get inserted username and password
            val familyID = joinFamilyId.text.toString()
            val familyPassword = hash(joinFamilyPassword.text.toString())
            if (checkValidFamily(familyID, familyPassword)) {
                // maybe get the shared inventory or add the current user's items to the shared inventory whichever is easier i guess
                Log.i(TAG, "$currentUser.username, $familyID")
                DataBaseHelper(this).modifyUserFamily(currentUser.username!!, familyID)
                startActivity(Intent(this, Settings::class.java))
            }
        }
    }

    private fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun checkValidFamily(familyID: String, familyPassword: String): Boolean {
        val db = DataBaseHelper(this)
        return if (db.verifyFamilyExists(familyID)) {
            if (db.verifyFamilyPassword(familyID, familyPassword)) { true
            } else {
                // LOOK AT THIS AND REMEMBER TO FIX IT @@@@@@@@@@@@@@@JETH
                // DONE @@@@@@@@@@@@@@@CLOUD
                Snackbar.make(joinFamilyClRoot, "Wrong Family Password", Snackbar.LENGTH_LONG).show()
                false
            }
        } else {
            Snackbar.make(joinFamilyClRoot, "A family with that ID does not exist", Snackbar.LENGTH_LONG).show()
            false
        }
    }
}