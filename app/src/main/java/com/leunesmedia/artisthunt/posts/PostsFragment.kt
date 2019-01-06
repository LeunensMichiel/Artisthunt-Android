package com.leunesmedia.artisthunt.posts


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.leunesmedia.artisthunt.MainActivity
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_posts.*

/**
 * Fragment that contains logic to display all Posts
 */
class PostsFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel

    /**
     * Creates view and initialises required viewmodels
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userViewModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        postViewModel = activity?.run {
            ViewModelProviders.of(this).get(PostViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }
    /**
     * Adds a LinearLayoutManager to rv_posts and applies an adapter
     * Observes uiMessage from postViewModel and updates UI accordingly
     * Observes allPosts from the Repository and calls the postViewModel to retrieve it from Server if Empty
     * Creates RefreshListener
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel.postRepo.allPosts.observe(this, Observer {
            if (it.isNullOrEmpty()){
                postViewModel.retrieveAllPosts()
            }
        })

        postViewModel.uiMessage.observe(this, Observer {
            when (it!!.data) {
                "retrieveAllPostsSucces" -> {
                }
                "retrieveAllPostsError" -> {
                    it.data = "Loading Error. Check Internet Connection"
                    Toast.makeText(activity as MainActivity, it.data, Toast.LENGTH_SHORT).show()
                }
            }
        })

        val viewManager = LinearLayoutManager((activity as MainActivity))
        val viewAdapter = PostAdapter(this, postViewModel, userViewModel)
        rv_posts.apply {
            layoutManager = viewManager
            adapter = viewAdapter
//            addItemDecoration(RV_PostDecorator(5))
        }

        postRefresher.setOnRefreshListener {
            postViewModel.retrieveAllPosts()
            postRefresher.isRefreshing = false
        }
    }
}
