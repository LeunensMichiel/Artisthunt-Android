package com.leunesmedia.artisthunt

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.leunesmedia.artisthunt.user.LoginFragment
import com.leunesmedia.artisthunt.user.RegisterFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var loginFragment: LoginFragment
    private lateinit var registerFragment: RegisterFragment

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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
}
