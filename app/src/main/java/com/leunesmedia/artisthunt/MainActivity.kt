package com.leunesmedia.artisthunt

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.leunesmedia.artisthunt.user.LoginFragment
import com.leunesmedia.artisthunt.user.RegisterFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var loginFragment: LoginFragment
    private lateinit var registerFragment: RegisterFragment

    private lateinit var userViewModel: UserViewModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_addPost -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        userViewModel.userRepo.user.observe(this, Observer<Model.User?> {
            if (it == null) {
                navigation.visibility = View.GONE
                loginFragment = LoginFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame, loginFragment)
                    .commit()
                showActionBar(false)

            } else {
                showActionBar(true)
                navigation.visibility = View.VISIBLE

                for (fragment in supportFragmentManager.fragments) {
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
            }


        })
    }

    fun toLogin(v: View) {
        loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame, loginFragment)
            .commit()
    }

    /**
     * This function replaces the login fragment with the register fragment
     */
    fun toRegister(v: View) {
        registerFragment = RegisterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame, registerFragment)
            .commit()
    }

    /**
     * This function replaces the login fragment with the forgotPassword fragment
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
            this.supportActionBar?.elevation = 0F
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
//
//            }
        } else {
            this.supportActionBar?.hide()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = Color.parseColor("#A3A29F")
//
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_logoutBtn -> {
                logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        userViewModel.userRepo.nukeUsers()
        getSharedPreferences(getString(R.string.sharedPreferenceUserDetailsKey), Context.MODE_PRIVATE)
            .edit()
            .remove(getString(R.string.userIdKey))
            .remove(getString(R.string.authTokenKey))
            .apply()
        navigation.visibility = View.GONE
        showActionBar(false)
        loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame, loginFragment)
            .commit()
    }
}
