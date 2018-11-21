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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mikhaellopez.circularimageview.CircularImageView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_setup.*


class SetupActivity : AppCompatActivity() {
    lateinit var img: CircularImageView
    lateinit var usrID: String
    lateinit var profilePic: Uri
    private var isChanged: Boolean = false
    lateinit var mAuth: FirebaseAuth
    lateinit var mStorage: StorageReference
    lateinit var mfireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        val toolbr = findViewById<android.support.v7.widget.Toolbar>(R.id.setupToolbar)
        setSupportActionBar(toolbr)

        supportActionBar!!.title = "Account Setup"

        mAuth = FirebaseAuth.getInstance()
        mStorage = FirebaseStorage.getInstance().reference
        mfireStore = FirebaseFirestore.getInstance()

        usrID = mAuth.currentUser!!.uid


        img = findViewById(R.id.setupImage)
        val setupTxf = findViewById<EditText>(R.id.setupName)
        val setupBtn = findViewById<Button>(R.id.setupBtn)
        val setupProgress = findViewById<ProgressBar>(R.id.setupProgress)

        setupBtn.isEnabled = false
        setupProgress.visibility = View.VISIBLE


        mfireStore.collection("Users").document(usrID).get().addOnCompleteListener() { t ->
            if (t.isSuccessful) {
                if (t.result.exists()) {
                    val usrname: String = t.result.getString("name")!!
                    val img: String = t.result.getString("image")!!

                    profilePic = Uri.parse(img)

                    setupName.setText(usrname)

                    val placeholder: RequestOptions = RequestOptions()
                    placeholder.placeholder(R.mipmap.profilepic_foreground)
                    Glide.with(this).setDefaultRequestOptions(placeholder).load(profilePic).into(setupImage)
                }
            } else {
                Toast.makeText(this, t.exception?.message, Toast.LENGTH_LONG).show()
            }
            setupProgress.visibility = View.INVISIBLE
            setupBtn.isEnabled = true
        }

        setupBtn.setOnClickListener() {
            val usrName = setupName.text.toString()
            if (usrName.isNotEmpty()) {

                setupProgress.visibility = View.VISIBLE

                if (isChanged) {
                    //mss ook checken op profilePic?
                    val imgPath: StorageReference = mStorage.child("profile_pictures").child(usrID + ".jpg")

                    imgPath.putFile(profilePic)
                        .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            return@Continuation imgPath.downloadUrl
                        }).addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            storeIntoFirebase(t, usrName, setupProgress)
                        } else {
                            Toast.makeText(this, t.exception?.message, Toast.LENGTH_LONG).show()
                            setupProgress.visibility = View.INVISIBLE
                        }
                    }
                } else {
                    storeIntoFirebase(null, usrName, setupProgress)
                }
            }
        }

        img.setOnClickListener() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                    )
                }
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
                    )
                } else {
                    selectImage()
                }
            } else {
                selectImage()
            }
        }
    }

    private fun storeIntoFirebase(
        t: Task<Uri>?,
        usrName: String,
        setupProgress: ProgressBar
    ) {
        val uri: Uri
        if (t != null) {
            uri = t.result
        } else {
            uri = profilePic
        }

        val USERS: MutableMap<String, Any> = HashMap()
        USERS.put("name", usrName)
        USERS.put("image", uri.toString())

        mfireStore.collection("Users").document(usrID).set(USERS).addOnCompleteListener() { t ->
            if (t.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, t.exception?.message, Toast.LENGTH_LONG).show()
            }
            setupProgress.visibility = View.INVISIBLE
        }
    }

    private fun selectImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                this.profilePic = result.uri
                setupImage.setImageURI(profilePic)

                isChanged = true
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
