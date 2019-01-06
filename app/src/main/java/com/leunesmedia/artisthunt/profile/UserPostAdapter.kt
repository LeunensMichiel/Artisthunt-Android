package com.leunesmedia.artisthunt.profile

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_userpost.view.*
/**
 * Adapter for user_posts
 */
class UserPostAdapter(
    lifecycleOwner: LifecycleOwner,
    val postViewModel: PostViewModel,
    val userViewModel: UserViewModel
) : RecyclerView.Adapter<UserPostAdapter.UserPostViewHolder>() {

    private val SERVER_IMG_URL = "http://projecten3studserver03.westeurope.cloudapp.azure.com:3001/images/"
    private var dataSet: List<Model.Post> = mutableListOf()

    /**
     * Observes a change in allPosts and notifies if it Changed so UI can update
     * Initialises dataSet if userPosts from ViewModel is not Null
     */
    init {
        if (postViewModel.userPosts.value != null) {
            dataSet = postViewModel.userPosts.value!!
        }
        postViewModel.userPosts.observe(lifecycleOwner, Observer {
            if (it != null) {
                dataSet = it
                notifyDataSetChanged()
            } else {
                dataSet = mutableListOf()
                notifyDataSetChanged()
            }
        })
    }
    /**
     * Uses item_userpost for recyclerview Items and returns UserPostViewHolder Class
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserPostAdapter.UserPostViewHolder {
        return UserPostViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_userpost, p0, false))
    }
    /**
     * returns count of userposts from postRepo
     */
    override fun getItemCount(): Int {
        return dataSet.size
    }
    /**
     * Binds Data from the userPosts to the UserPostViewHolder
     * Checks if images has to be displayed or not and loads them with Picasso
     */
    override fun onBindViewHolder(p0: UserPostAdapter.UserPostViewHolder, p1: Int) {
        if (dataSet[p1].post_image_filename != null) {
            p0.title.text = ""
            //Recyclerview can get messy with async image retrieving so we have to make sure the previous request is cancelled when scrolling
            Picasso.get().cancelRequest(p0.image)
            Picasso.get()
                .load(SERVER_IMG_URL + dataSet[p1].post_image_filename)
                .resize(100, 100)
                .centerCrop()
                .into(p0.image)
        } else {
            p0.image.setImageDrawable(null)
            p0.title.text = dataSet[p1].title
        }
    }
    /**
     * ViewHolder Class for UserPosts that gets required Items to bind data to
     */
    inner class UserPostViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.item_user_post_text
        val image: ImageView = v.userpost_image
    }

}