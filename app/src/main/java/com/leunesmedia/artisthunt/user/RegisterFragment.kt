package com.leunesmedia.artisthunt.user

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
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.uiMessage.observe(this, Observer {
            when (it!!.data) {
                "registerSucces" -> {
                    Progress_register.visibility = View.GONE
                    it.data = "Signed up"
                    activity!!.getSharedPreferences(
                        getString(R.string.sharedPreferenceUserDetailsKey),
                        Context.MODE_PRIVATE
                    ).edit()
                        .putString(getString(R.string.authTokenKey), userViewModel.userRepo.user.value?.token)
                        .putString(getString(R.string.userIdKey), userViewModel.userRepo.user.value?._id)
                        .apply()
                    Toast.makeText(activity as MainActivity, it.data, Toast.LENGTH_SHORT).show()
                }
                "registerError" -> {
                    Progress_register.visibility = View.GONE
                    focusView = register_repeat_password
                    register_repeat_password.error = getString(R.string.error)
                }
            }
        })

        register_repeat_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptRegister()
                return@OnEditorActionListener true
            }
            false
        })

        btn_register.setOnClickListener { attemptRegister() }
    }

    private fun attemptRegister() {
// Reset errors.
        register_email.error = null
        register_repeat_password.error = null
        register_firstname.error = null
        register_lastname.error = null

        // Check for a valid password, if the user entered one.
        if (register_password.text.isEmpty()) {
            register_password.error = getString(R.string.error_field_required)
            focusView = register_password
            cancel = true
        }
        if (register_repeat_password.text.isEmpty()) {
            register_repeat_password.error = getString(R.string.error_field_required)
            focusView = register_repeat_password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(register_email.text.toString())) {
            register_email.error = getString(R.string.error_field_required)
            focusView = register_email
            cancel = true
        } else if (!isEmailValid(register_email.text.toString())) {
            register_email.error = getString(R.string.error_invalid_email)
            focusView = register_email
            cancel = true
        }

        if (TextUtils.isEmpty(register_firstname.text.toString())) {
            register_firstname.error = getString(R.string.error_field_required)
            focusView = register_firstname
            cancel = true
        }

        if (TextUtils.isEmpty(register_lastname.text.toString())) {
            register_lastname.error = getString(R.string.error_field_required)
            focusView = register_lastname
            cancel = true
        }

        if (register_password.text.toString() != register_repeat_password.text.toString()) {
            register_repeat_password.error = getString(R.string.not_same_password)
            focusView = register_repeat_password
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            val registerDetails = Model.Register(
                register_email.text.toString(),
                register_repeat_password.text.toString(),
                register_firstname.text.toString(),
                register_lastname.text.toString()
            )
            toRegister(registerDetails)
        }
    }

    private fun toRegister(registerDetails: Model.Register) {
        userViewModel.register(registerDetails)
        Progress_register.visibility = View.VISIBLE
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
