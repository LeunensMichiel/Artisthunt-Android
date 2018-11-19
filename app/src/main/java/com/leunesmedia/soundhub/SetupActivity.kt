package com.leunesmedia.soundhub

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mikhaellopez.circularimageview.CircularImageView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_setup.*


class SetupActivity : AppCompatActivity() {
    lateinit var img: CircularImageView
    lateinit var profilePic : Uri
    lateinit var mAuth: FirebaseAuth
    lateinit var mStorage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        val toolbr = findViewById<android.support.v7.widget.Toolbar>(R.id.setupToolbar)
        setSupportActionBar(toolbr)

        supportActionBar!!.title = "Account Setup"

        mAuth = FirebaseAuth.getInstance()
        mStorage = FirebaseStorage.getInstance().reference


        img = findViewById(R.id.setupImage)
        val setupTxf = findViewById<EditText>(R.id.setupName)
        val setupBtn = findViewById<Button>(R.id.setupBtn)
        val setupProgress = findViewById<ProgressBar>(R.id.setupProgress)

        setupBtn.setOnClickListener() {
            val usrnName = setupName.text.toString()

            //mss ook checken op profilePic?
            if (usrnName.isNotEmpty() ) {
                val usrID: String = mAuth.currentUser!!.uid
                val imgPath : StorageReference = mStorage.child("profile_pictures").child(usrID + ",jpg")
                setupProgress.visibility = View.VISIBLE

                imgPath.putFile(profilePic).addOnCompleteListener() { t ->
                    if (t.isSuccessful) {
                        val uri : Task<Uri> = t.result.storage.downloadUrl
                    } else {
                        Toast.makeText(this, t.exception?.message, Toast.LENGTH_LONG).show()
                    }

                    setupProgress.visibility = View.INVISIBLE
                }
            }
        }

        img.setOnClickListener() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                } else {
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                this.profilePic = result.uri
                setupImage.setImageURI(profilePic)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
