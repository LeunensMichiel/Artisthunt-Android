package com.leunesmedia.artisthunt.addpost


import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.leunesmedia.artisthunt.MainActivity
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.domain.PostType
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_add_post_image.*
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.imageResource
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddPostImageFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel
    private var imageFile: File? = null
    private var image: Bitmap? = null
    private var cancel = false
    private var focusView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        postViewModel = activity?.run {
            ViewModelProviders.of(this).get(PostViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postViewModel.uiMessage.observe(this, android.arch.lifecycle.Observer {
            when (it!!.data) {
                "addPostImageSucces" -> {
                    progressBar_AddPostImage.visibility = View.INVISIBLE
                    it.data = "Post Added!"
                    addPostImage_Title.text.clear()
                    addPostImage_description.text.clear()
                    addPostImage_Image.imageResource = R.drawable.ic_addimage
                    (activity as MainActivity).popStack()
                    Toast.makeText(activity as MainActivity, it.data, Toast.LENGTH_SHORT).show()
                }
                "addPostImageError" -> {
                    progressBar_AddPostImage.visibility = View.INVISIBLE
                    Toast.makeText(activity as MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    focusView = addPostImage_description
                }
            }
        })

        addPostImage_PostBtn.setOnClickListener {
            attemptToPost()
        }

        addPostImage_Image.setOnClickListener {
            initImageUpload()
        }
    }

    private fun initImageUpload() {

        addPostImage_Image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        activity as MainActivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity as MainActivity,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                    )
                }
                if (ContextCompat.checkSelfPermission(
                        activity as MainActivity,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity as MainActivity,
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

    private fun selectImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .start(context!!, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, result.uri)

                val file = File(context?.cacheDir, "temp")
                val bos = ByteArrayOutputStream()
                file.createNewFile()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val fos = FileOutputStream(file)

                fos.write(bos.toByteArray())
                fos.flush()
                fos.close()

                addPostImage_Image.imageBitmap = bitmap

                image = bitmap
                imageFile = file
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity as MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun attemptToPost() {
        addPostImage_Title.error = null
        addPostImage_description.error = null

        if (TextUtils.isEmpty(addPostImage_Title.text.toString())) {
            addPostImage_Title.error = getString(R.string.error_field_required)
            focusView = addPostImage_Title
            cancel = true
        }
        if (TextUtils.isEmpty(addPostImage_description.text.toString())) {
            addPostImage_description.error = getString(R.string.error_field_required)
            focusView = addPostImage_description
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            doPost()
        }
    }

    private fun doPost() {
        progressBar_AddPostImage.visibility = View.VISIBLE
        if (image == null || imageFile == null) {
            val post = Model.Post(
                user_id = userViewModel.userRepo.user.value?._id,
                title = addPostImage_Title.text.toString(),
                description = addPostImage_description.text.toString(),
                type = PostType.TEXT.toString(),
                date = Calendar.getInstance().time
            )
            postViewModel.addPost(post)
        } else {
            val post = Model.Post(
                user_id = userViewModel.userRepo.user.value?._id,
                title = addPostImage_Title.text.toString(),
                description = addPostImage_description.text.toString(),
                type = PostType.IMAGE.toString(),
                date = Calendar.getInstance().time
            )
            postViewModel.addImagePost(imageFile!!, image!!, post)
        }

    }


}
