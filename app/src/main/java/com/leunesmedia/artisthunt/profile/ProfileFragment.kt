package com.leunesmedia.artisthunt.profile


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.leunesmedia.artisthunt.MainActivity
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.leunesmedia.artisthunt.utils.RV_UserPostsDecorator
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel
    private val SERVER_IMG_URL = "http://projecten3studserver03.westeurope.cloudapp.azure.com:3001/images/"


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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel.userPosts.observe(this, Observer {
            if (it == null || it.isEmpty()) {
                postViewModel.retrieveUserPosts()
                empty_userposts.visibility = View.VISIBLE
                rv_userPosts.visibility = View.GONE
            } else {
                empty_userposts.visibility = View.GONE
                rv_userPosts.visibility = View.VISIBLE
            }
        })

        userViewModel.userRepo.user.observe(this, Observer {
            postViewModel.retrieveUserPosts()
            profileFragment_profilename.text =
                    "${userViewModel.userRepo.user.value?.firstname} ${userViewModel.userRepo.user.value?.lastname}"
            profileFragment_favGenreText.text = "Punk Rock"
            profileFragment_favArtistText.text = "Foo Fighters"
            profileFragment_favInstrumentText.text = "Guitar"

            if (it!!.profile_image_filename != null) {
                Picasso.get()
                    .load(SERVER_IMG_URL + userViewModel.userRepo.user.value?.profile_image_filename!!)
                    .placeholder(R.drawable.person_icon)
                    .resize(250, 250)
                    .centerCrop()
                    .into(profileFragment_profilepic)
            }
        })

        profileFragment_profilepic.setOnClickListener {
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

        var viewManager : RecyclerView.LayoutManager
        if ((activity as MainActivity).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewManager = GridLayoutManager((activity as MainActivity), 3)
        } else {
            viewManager = GridLayoutManager((activity as MainActivity), 5)
        }

        val viewAdapter = UserPostAdapter(this, postViewModel, userViewModel)
        rv_userPosts.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(RV_UserPostsDecorator(5))
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

                userViewModel.changeProfilePicture(file, bitmap)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity as MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
