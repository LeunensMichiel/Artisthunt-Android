package com.leunesmedia.artisthunt.authentication


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.leunesmedia.artisthunt.MainActivity
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private var cancel = false
    private var focusView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.uiMessage.observe(this, Observer {
            when (it!!.data) {
                "loginSucces" -> {
                    Progress_login.visibility = View.GONE
                    it.data = "Signed in"
                    activity!!.getSharedPreferences(
                        getString(R.string.sharedPreferenceUserDetailsKey),
                        Context.MODE_PRIVATE
                    ).edit()
                        .putString(getString(R.string.authTokenKey), userViewModel.userRepo.user.value?.token)
                        .putString(getString(R.string.userIdKey), userViewModel.userRepo.user.value?._id)
                        .apply()
                    Toast.makeText(activity as MainActivity, it.data, Toast.LENGTH_SHORT).show()
                }
                "loginError" -> {
                    Progress_login.visibility = View.GONE
                    focusView = login_password
                    login_password.error = getString(R.string.error)
                }
            }
        })

        login_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {
        // Reset errors.
        loginEmail.error = null
        login_password.error = null
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(login_password.text.toString())) {
            login_password.error = getString(R.string.error_field_required)
            focusView = login_password
            cancel = true

        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(loginEmail.text.toString())) {
            loginEmail.error = getString(R.string.error_field_required)
            focusView = loginEmail
            cancel = true
        } else if (!isEmailValid(loginEmail.text.toString())) {
            loginEmail.error = getString(R.string.error_invalid_email)
            focusView = loginEmail
            cancel = true
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            val login = Model.Login(loginEmail.text.toString(), login_password.text.toString())
            doLogin(login)
        }
    }

    /**
     * Here we call the UserViewModel to do the login for us in an async method
     * @param loginDetails we give a unique model that has a password and email so the backend can convert it to JSON
     */
    private fun doLogin(loginDetails: Model.Login) {
        userViewModel.login(loginDetails)
        Progress_login.visibility = View.VISIBLE
    }

    /**
     * Check's if email is valid
     * Email is valid when it has a '@' sign
     *
     * no regex
     * From Stack Overflow: Apparently the following is a reg-ex that correctly validates most e-mails addresses that conform to RFC 2822,
     * (and will still fail on things like "user@gmail.com.nospam", as will org.apache.commons.validator.routines.EmailValidator)
     * @param email is the email that will be checked
     */
    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }
}
