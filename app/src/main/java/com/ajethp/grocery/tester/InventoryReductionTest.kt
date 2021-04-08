package com.ajethp.grocery.tester
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import com.ajethp.grocery.Login
import org.jetbrains.annotations.TestOnly

class validUser {
    @Test
    fun unitTest(){
        assertThat(Login.checkValidUser("coloteong", "claudia")).isTrue()
        assertThat(checkValidUser("jeth", "isuck")).isFalse()
        assertThat(checkValidUser("cloudy", "claudia")).isFalse()
        assertThat(checkValidUser("jethro", "byebye")).isFalse()
    }
}