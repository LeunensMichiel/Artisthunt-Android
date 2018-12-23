package com.leunesmedia.artisthunt.profile

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.item_userpost.view.*

class UserPostAdapter(
    lifecycleOwner: LifecycleOwner,
    val postViewModel: PostViewModel,
    val userViewModel: UserViewModel
) : RecyclerView.Adapter<UserPostAdapter.UserPostViewHolder>() {

    init {
        postViewModel.userPosts.observe(lifecycleOwner, Observer {
            notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserPostAdapter.UserPostViewHolder {
        return UserPostViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_userpost, p0, false))
    }

    override fun getItemCount(): Int {
        if (postViewModel.userPosts.value.isNullOrEmpty()) {
            return 0
        }
        return postViewModel.userPosts.value?.size!!
    }

    override fun onBindViewHolder(p0: UserPostAdapter.UserPostViewHolder, p1: Int) {
        p0.title.text = postViewModel.userPosts.value!![p1].title
    }

    inner class UserPostViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.item_user_post_text
    }

}