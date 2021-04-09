package com.ajethp.grocery.tester

import android.net.sip.SipSession
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ajethp.grocery.Login
// import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {
    private var result = true
    @Test fun loginWrongUNWrongPassword() {
        val scenario = launch(Login::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidUser("Elrou", "ElroyAn")
        }
        assertEquals(result, false)
    }

    @Test fun loginCorrectUNWrongPassword() {
        val scenario = launch(Login::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidUser("Elroy", "ElroyAn")
        }
        assertEquals(result, false)
    }

    @Test fun loginCorrectPasswordWrongUN() {
        val scenario = launch(Login::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidUser("Elry", "elroy123")
        }
        assertEquals(result, false)
    }

    @Test fun loginCorrectPasswordCorrectUN() {
        val scenario = launch(Login::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidUser("Elroy", "elroy123")
        }
        assertEquals(result, false)
    }
}