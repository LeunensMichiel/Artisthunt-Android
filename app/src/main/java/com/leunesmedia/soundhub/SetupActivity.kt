package com.leunesmedia.soundhub

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.mikhaellopez.circularimageview.CircularImageView

class SetupActivity : AppCompatActivity() {
    lateinit var img: CircularImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        val toolbr = findViewById<android.support.v7.widget.Toolbar>(R.id.setupToolbar)
        setSupportActionBar(toolbr)

        supportActionBar!!.title = "Account Setup"


        img = findViewById(R.id.setupImage)
        img.setOnClickListener() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                } else {
                    Toast.makeText(this, "You Already Have Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
