package com.leunesmedia.artisthunt.addpost


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_add_post_text.*
import java.util.*

class AddPostTextFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel
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
        return inflater.inflate(R.layout.fragment_add_post_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postViewModel.uiMessage.observe(this, android.arch.lifecycle.Observer {
            when (it!!.data) {
                "addPostsSucces" -> {
                    progressBar_AddPostText.visibility = View.INVISIBLE
                    it.data = "Post Added!"
                    addPostText_Title.text.clear()
                    addPostText_description.text.clear()
                    (activity as MainActivity).popStack()
                    Toast.makeText(activity as MainActivity, it.data, Toast.LENGTH_SHORT).show()
                }
                "addPostsError" -> {
                    progressBar_AddPostText.visibility = View.INVISIBLE
                    Toast.makeText(activity as MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    focusView = addPostText_description
                }
            }
        })

        addPostText_PostBtn.setOnClickListener {
            attemptToPost()
        }
    }

    private fun attemptToPost() {
        addPostText_Title.error = null
        addPostText_description.error = null

        if (TextUtils.isEmpty(addPostText_Title.text.toString())) {
            addPostText_Title.error = getString(R.string.error_field_required)
            focusView = addPostText_Title
            cancel = true
        }
        if (TextUtils.isEmpty(addPostText_description.text.toString())) {
            addPostText_description.error = getString(R.string.error_field_required)
            focusView = addPostText_description
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            doPost()
        }
    }

    private fun doPost() {
        progressBar_AddPostText.visibility = View.VISIBLE
        val post = Model.Post(
            user_id = userViewModel.userRepo.user.value?._id,
            title = addPostText_Title.text.toString(),
            description = addPostText_description.text.toString(),
            type = PostType.TEXT.toString(),
            date = Calendar.getInstance().time
        )
        postViewModel.addPost(post)
    }


}
