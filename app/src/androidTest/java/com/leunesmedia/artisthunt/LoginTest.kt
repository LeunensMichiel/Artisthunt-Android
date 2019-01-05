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
class LoginTest {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var userViewModel: UserViewModel

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private fun resetAll() {
        userViewModel = ViewModelProviders.of(mActivityTestRule.activity).get(UserViewModel::class.java)
        userViewModel.userRepo.nukeUsers()

        sharedPreferences = mActivityTestRule.activity.getSharedPreferences(mActivityTestRule.activity.getString(R.string.sharedPreferenceUserDetailsKey), Context.MODE_PRIVATE)
        sharedPreferences
            .edit()
            .remove(mActivityTestRule.activity.getString(R.string.userIdKey))
            .remove(mActivityTestRule.activity.getString(R.string.authTokenKey))
            .apply()

    }

    private fun doLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.loginEmail))
            .perform(ViewActions.typeText("michiel.leunens@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.login_password))
            .perform(ViewActions.typeText("logmein"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.email_sign_in_button)).perform(ViewActions.scrollTo()).perform(
            ViewActions.closeSoftKeyboard(), ViewActions.click())
    }

    @Before
    fun setUp() {
        resetAll()
    }

    @Test
    fun containsEmailInput() {
        Espresso.onView(ViewMatchers.withId(R.id.loginEmail)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsPasswordInput() {
        Espresso.onView(ViewMatchers.withId(R.id.login_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsLoginButton() {
        Espresso.onView(ViewMatchers.withId(R.id.email_sign_in_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsRegisterButton() {
        Espresso.onView(ViewMatchers.withId(R.id.login_form_register_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun containsForgotPasswordButton() {
        Espresso.onView(ViewMatchers.withId(R.id.fragment_login_forgotbtn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun canTypeEmail() {
        Espresso.onView(ViewMatchers.withId(R.id.loginEmail))
            .perform(ViewActions.typeText("michiel.leunens@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.loginEmail))
            .check(ViewAssertions.matches(ViewMatchers.withText("michiel.leunens@gmail.com")))
    }

    @Test
    fun canTypePassword() {
        Espresso.onView(ViewMatchers.withId(R.id.login_password))
            .perform(ViewActions.typeText("logmein"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.login_password))
            .check(ViewAssertions.matches(ViewMatchers.withText("logmein")))
    }

    @Test
    fun registerButtonShouldGoToRegisterScreen() {
        Espresso.onView(ViewMatchers.withId(R.id.login_form_register_btn)).perform(ViewActions.scrollTo()).perform(
            ViewActions.closeSoftKeyboard(), ViewActions.click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.register_email)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun successfulLoginShouldRedirectToPosts() {
        doLogin()
        Espresso.onView(ViewMatchers.withId(R.id.navigation))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun tearDown() {
        resetAll()
    }
}
