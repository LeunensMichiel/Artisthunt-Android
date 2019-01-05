package com.leunesmedia.artisthunt

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterTest {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var userViewModel: UserViewModel

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private fun resetData() {
        userViewModel = ViewModelProviders.of(mActivityTestRule.activity).get(UserViewModel::class.java)
        userViewModel.userRepo.nukeUsers()

        sharedPreferences = mActivityTestRule.activity.getSharedPreferences(mActivityTestRule.activity.getString(R.string.sharedPreferenceUserDetailsKey), Context.MODE_PRIVATE)
        sharedPreferences
            .edit()
            .remove(mActivityTestRule.activity.getString(R.string.userIdKey))
            .remove(mActivityTestRule.activity.getString(R.string.authTokenKey))
            .apply()
    }

    @Before
    fun setUp() {
        resetData()

        Espresso.onView(ViewMatchers.withId(R.id.login_form_register_btn)).perform(ViewActions.scrollTo()).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )
    }

    @Test
    fun containsEmailField() {
        Espresso.onView(ViewMatchers.withId(R.id.register_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsFirstnameField() {
        Espresso.onView(ViewMatchers.withId(R.id.register_firstname))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun containsLastnameField() {
        Espresso.onView(ViewMatchers.withId(R.id.register_lastname))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun containsPasswordField() {
        Espresso.onView(ViewMatchers.withId(R.id.register_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsRepeatPasswordField() {
        Espresso.onView(ViewMatchers.withId(R.id.register_repeat_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsRegisterButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_register))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsLoginButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btnBackToLogin))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun canTypeEmail() {
        Espresso.onView(ViewMatchers.withId(R.id.register_email))
            .perform(ViewActions.typeText("test@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_email))
            .check(ViewAssertions.matches(ViewMatchers.withText("test@gmail.com")))
    }

    @Test
    fun canTypeFirstname() {
        Espresso.onView(ViewMatchers.withId(R.id.register_firstname))
            .perform(ViewActions.typeText("Test"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_firstname))
            .check(ViewAssertions.matches(ViewMatchers.withText("Test")))
    }

    @Test
    fun canTypeLastname() {
        Espresso.onView(ViewMatchers.withId(R.id.register_lastname))
            .perform(ViewActions.typeText("Test"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_lastname))
            .check(ViewAssertions.matches(ViewMatchers.withText("Test")))
    }

    @Test
    fun canTypePassword() {
        Espresso.onView(ViewMatchers.withId(R.id.register_password))
            .perform(ViewActions.typeText("logmein"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_password))
            .check(ViewAssertions.matches(ViewMatchers.withText("logmein")))
    }

    @Test
    fun canTypeRepeatPassword() {
        Espresso.onView(ViewMatchers.withId(R.id.register_repeat_password))
            .perform(ViewActions.typeText("logmein"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_repeat_password))
            .check(ViewAssertions.matches(ViewMatchers.withText("logmein")))
    }

    @Test
    fun successfulRegisterOpensPostFragment() {
        register()
        Espresso.onView(ViewMatchers.withId(R.id.navigation))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun register() {
        Espresso.onView(ViewMatchers.withId(R.id.register_email))
            .perform(ViewActions.typeText("test@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_firstname))
            .perform(ViewActions.typeText("Michiel"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_lastname))
            .perform(ViewActions.typeText("Leunens"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_password))
            .perform(ViewActions.typeText("logmein"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.register_repeat_password))
            .perform(ViewActions.typeText("logmein"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_register)).perform(ViewActions.scrollTo()).perform(
            ViewActions.closeSoftKeyboard(), ViewActions.click())
    }

    @Test
    fun loginButtonShouldOpenLoginScreen() {
        Espresso.onView(ViewMatchers.withId(R.id.btnBackToLogin))
            .perform(ViewActions.scrollTo()).perform(ViewActions.closeSoftKeyboard() ,ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.loginTitel)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun tearDown() {
        resetData()
    }

}
