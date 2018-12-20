package com.leunesmedia.artisthunt.posts

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
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat

class PostAdapter(
    lifecycleOwner: LifecycleOwner,
    val postViewModel: PostViewModel,
    val userViewModel: UserViewModel
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

//    private var dataSet: Array<Model.Post> = arrayOf()
    val formatter = SimpleDateFormat("dd-MM-yyyy  hh:mm")


    init {
        postViewModel.postRepo.allPosts.observe(lifecycleOwner, Observer {
            notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_post, p0, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (postViewModel.postRepo.allPosts.value.isNullOrEmpty()) {
            return 0
        }
        return postViewModel.postRepo.allPosts.value?.size!!
    }

    override fun onBindViewHolder(p0: PostViewHolder, p1: Int) {
        p0.title.text = postViewModel.postRepo.allPosts.value!![p1].title
        p0.description.text = postViewModel.postRepo.allPosts.value!![p1].description

        p0.date.text = formatter.format( postViewModel.postRepo.allPosts.value!![p1].date)
    }


    inner class PostViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.post_Title
        val description: TextView = v.post_Description
        val date: TextView = v.post_Date
    }

}