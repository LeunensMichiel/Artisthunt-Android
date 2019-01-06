package com.leunesmedia.artisthunt

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.leunesmedia.artisthunt.addpost.AddPostFragment
import com.leunesmedia.artisthunt.authentication.LoginFragment
import com.leunesmedia.artisthunt.authentication.RegisterFragment
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.leunesmedia.artisthunt.posts.PostsFragment
import com.leunesmedia.artisthunt.profile.ProfileFragment
import com.leunesmedia.artisthunt.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The Main Activity. Everything from the app is routed through here
 */
class MainActivity : AppCompatActivity() {
    private lateinit var loginFragment: LoginFragment
    private lateinit var registerFragment: RegisterFragment
    private lateinit var postFragment: PostsFragment
    private lateinit var addPostFragment: AddPostFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var settingsFragment: SettingsFragment

    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel

    /**
     * Listener that checks which fragment should be displayed when clicking on a BottomNavigationBarItem
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame, postFragment, "post")
                    .addToBackStack(null)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_addPost -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame, addPostFragment, "addpost")
                    .addToBackStack(null)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame, profileFragment, "profile")
                    .addToBackStack(null)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    /**
     * Sets Theme to artisttheme
     * Sets Colour of the Status Bar to colorPrimary
     * Sets custom Toolbar and sets elevation off
     *
     * Initialises Navigation with its listener and sets default Item to Home
     * Initialises ViewModels
     * calls (isLoggedIn)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.artist)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        supportActionBar!!.elevation = 0F


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.findItem(R.id.navigation_home).isChecked = true
        navigation.visibility = View.GONE

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        postViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)

        isLoggedIn()
    }

    /**
     * Observers the User in the UserViewModel's Repo.
     * If Token is null The LoginFragment will be shown,
     * Otherwise all other fragments are loaded into view and PostsFragment is shown, token is retrieved
     *
     * Also checks if Statusbar has to be displayed
     */
    private fun isLoggedIn() {
        userViewModel.userRepo.user.observe(this, Observer<Model.User?> {
            if (it?.token == null) {
                navigation.visibility = View.GONE
                loginFragment = LoginFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame, loginFragment, "login")
                    .commit()
                showActionBar(false)

            } else {
                getSharedPreferences(
                    getString(R.string.sharedPreferenceUserDetailsKey),
                    Context.MODE_PRIVATE
                ).edit()
                    .putString(getString(R.string.authTokenKey), userViewModel.userRepo.user.value?.token)
                    .putString(getString(R.string.userIdKey), userViewModel.userRepo.user.value?._id)
                    .apply()
                showActionBar(true)
                navigation.visibility = View.VISIBLE
                postFragment = PostsFragment()
                addPostFragment = AddPostFragment()
                profileFragment = ProfileFragment()
                settingsFragment = SettingsFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame, postFragment, "post")
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    /**
     * Anko Method to delegate User to LoginScreen
     */
    fun toLogin(v: View) {
        loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame, loginFragment, "login")
            .commit()
    }

    /**
     * Anko Method to delegate User to RegisterScreen
     */
    fun toRegister(v: View) {
        registerFragment = RegisterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame, registerFragment, "register")
            .commit()
    }

    /**
     * Anko function replaces the login fragment with the forgotPassword fragment
     */
//    fun toForgot(v : View) {
//        forgotPasswordFragment = ForgotPasswordFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.session_container, forgotPasswordFragment)
//            .commit()
//    }

    private fun showActionBar(bool: Boolean) {
        if (bool) {
            this.supportActionBar?.show()
        } else {
            this.supportActionBar?.hide()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * If User clicks Logout, (logout) is called
     * If User Clicks settings, Settings fragment replaces current fragment
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_logoutBtn -> {
                logout()
                return true
            }
            R.id.menu_settingsBtn -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame, settingsFragment, "settings")
                    .addToBackStack(null)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Clears Sharedpreferences, Deletes User from Room Database
     * Hides Navigation, Toolbar and redirects to Login
     */
    private fun logout() {
        getSharedPreferences(getString(R.string.sharedPreferenceUserDetailsKey), Context.MODE_PRIVATE)
            .edit()
            .remove(getString(R.string.userIdKey))
            .remove(getString(R.string.authTokenKey))
            .apply()
        userViewModel.userRepo.nukeUsers()

        navigation.visibility = View.GONE
        showActionBar(false)

        loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame, loginFragment)
            .commit()
    }

    /**
     * Function that fragments can call to remove themselves
     */
    fun popStack() {
        supportFragmentManager.popBackStack()
    }

}
