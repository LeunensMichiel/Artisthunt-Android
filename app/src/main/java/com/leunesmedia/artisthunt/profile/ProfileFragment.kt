package com.leunesmedia.artisthunt.profile


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leunesmedia.artisthunt.MainActivity
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.leunesmedia.artisthunt.utils.RV_UserPostsDecorator
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel

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

        userViewModel.userRepo.user.observe(this, Observer {
            postViewModel.retrieveUserPosts()
            profileFragment_profilename.text = "${userViewModel.userRepo.user.value?.firstname} ${userViewModel.userRepo.user.value?.lastname}"
            profileFragment_favGenreText.text = "Punk Rock"
            profileFragment_favArtistText.text = "Foo Fighters"
            profileFragment_favInstrumentText.text = "Guitar"
        })

        val viewManager = GridLayoutManager((activity as MainActivity), 3)
        val viewAdapter = UserPostAdapter(this, postViewModel, userViewModel)
        rv_userPosts.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(RV_UserPostsDecorator(5))
        }
    }


}
