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

class RegisterActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        val reg_email_field = findViewById<EditText>(R.id.registerEmail)
        val reg_pass_field = findViewById<EditText>(R.id.registerPassword)
        val reg_confirm_field = findViewById<EditText>(R.id.registerPasswordAgain)
        val reg_RegisterBtn = findViewById<Button>(R.id.buttonRegisterOnRegisterPage)
        val reg_loginBtn = findViewById<Button>(R.id.buttonBackToLogin)
        val progress = findViewById<ProgressBar>(R.id.regProgress)

        reg_RegisterBtn.setOnClickListener() {
            if (reg_email_field.text.toString().isNotEmpty() && reg_pass_field.text.toString().isNotEmpty() && reg_confirm_field.toString().isNotEmpty()) {
                if (reg_pass_field.text.toString().equals(reg_confirm_field.text.toString())) {
                    progress.visibility = View.VISIBLE
                    mAuth.createUserWithEmailAndPassword(
                        reg_email_field.text.toString(),
                        reg_pass_field.text.toString()
                    ).addOnCompleteListener { t ->
                        run {
                            if (t.isSuccessful) {
                                Toast.makeText(this, "Registry Succesful", Toast.LENGTH_SHORT).show()
                                sentToSetup()
                            } else {
                                Toast.makeText(this, t.exception?.message, Toast.LENGTH_LONG).show()
                            }
                            progress.visibility = View.VISIBLE
                        }
                    }

                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }

            }
        }

        reg_loginBtn.setOnClickListener() {
            sendToLogin()
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

    private fun sendToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun sentToSetup() {
        val intent = Intent(this, SetupActivity::class.java)
        startActivity(intent)
        finish()
    }
}
