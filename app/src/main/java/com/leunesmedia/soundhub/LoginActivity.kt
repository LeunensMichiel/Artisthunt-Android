package com.leunesmedia.soundhub

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val loginEmailText = findViewById<EditText>(R.id.loginEmail)
        val loginPassText = findViewById<EditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.buttonLogin)
        val regBtn = findViewById<Button>(R.id.buttonRegister)
        val procbar = findViewById<ProgressBar>(R.id.loginProgress)

        loginBtn.setOnClickListener { v ->
            if (loginEmailText.text.isNotEmpty() && loginPassText.text.isNotEmpty()) {
                procbar.visibility = View.VISIBLE

                mAuth.signInWithEmailAndPassword(loginEmailText.text.toString(), loginPassText.text.toString())
                    .addOnCompleteListener() { t ->
                        if (t.isSuccessful) {
                            Toast.makeText(this, "Login Succesful", Toast.LENGTH_SHORT).show()
                            sendToMain()
                        } else {
                            Toast.makeText(this, t.exception?.message, Toast.LENGTH_LONG).show()
                        }
                        procbar.visibility = View.INVISIBLE
                    }
            }
        }
        buttonRegister.setOnClickListener() {
            sendToRegister()
        }
    }

    override fun onStart() {
        super.onStart()

        var currentUser: FirebaseUser? = mAuth.currentUser

        if (currentUser != null) {
            sendToMain()
        }
    }

    private fun sendToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun sendToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}
