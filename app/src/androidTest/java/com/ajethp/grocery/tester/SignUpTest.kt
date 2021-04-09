package com.ajethp.grocery.tester

import android.net.sip.SipSession
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ajethp.grocery.SignUp
// import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpTest {
    private var result = 0
    @Test
    fun signupExistingUsername() {
        val scenario = launch(SignUp::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidLoginDetails(
                "claudia@gmail.com",
                "jethro",
                "claudia",
                "claudia"
            )
        }
        assertEquals(result, 5)
    }

    @Test
    fun signupPasswordNotMatching() {
        val scenario = launch(SignUp::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidLoginDetails(
                "claudia@gmail.com",
                "cloudy",
                "claudia1",
                "claudia"
            )
        }
        assertEquals(result, 4)
    }

    @Test
    fun signupEmptyPassword() {
        val scenario = launch(SignUp::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidLoginDetails("claudia@gmail.com", "coloteong", "", "")
        }
        assertEquals(result, 3)
    }

    @Test
    fun signupEmptyUsername() {
        val scenario = launch(SignUp::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidLoginDetails("claudia@gmail.com", "", "claudia", "claudia")
        }
        assertEquals(result, 2)
    }

    @Test
    fun signupInvalidEmail() {
        val scenario = launch(SignUp::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidLoginDetails(
                "claudia.com",
                "coloteong",
                "claudia",
                "claudia"
            )
        }
        assertEquals(result, 1)
    }

    @Test fun validLoginDetails() {
        val scenario = launch(SignUp::class.java)
        scenario.onActivity { activity ->
            result = activity.checkValidLoginDetails("claudia@gmail.com", "coloteong123", "claudia", "claudia")
        }
        assertEquals(result, 6)
    }
}

