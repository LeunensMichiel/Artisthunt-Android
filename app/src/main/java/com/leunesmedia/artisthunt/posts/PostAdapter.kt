package com.leunesmedia.artisthunt.posts

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.leunesmedia.artisthunt.R
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.domain.PostType
import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat

class PostAdapter(
    lifecycleOwner: LifecycleOwner,
    val postViewModel: PostViewModel,
    val userViewModel: UserViewModel
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val formatter = SimpleDateFormat("dd-MM-yyyy  hh:mm")
    private val SERVER_IMG_URL = "http://projecten3studserver03.westeurope.cloudapp.azure.com:3001/images/"

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
        p0.date.text = formatter.format(postViewModel.postRepo.allPosts.value!![p1].date)
        p0.counter.text = postViewModel.postRepo.allPosts.value!![p1].likers.size.toString()
        if (postViewModel.postRepo.allPosts.value!![p1].likers.contains(userViewModel.userRepo.user.value!!._id)) {
            p0.heart.frame = 140
        } else {
            p0.heart.frame = 0
        }
        p0.heart.setOnClickListener {
            if (postViewModel.postRepo.allPosts.value!![p1].likers.contains(userViewModel.userRepo.user.value!!._id)) {
                //The user has already liked the post so unlikes it
                p0.heart.setMaxFrame(149)
                p0.heart.frame = 140
                p0.heart.resumeAnimation()
                p0.counter.text = (postViewModel.postRepo.allPosts.value!![p1].likers.size - 1).toString()
            } else {
                //The user has not liked the post yet so likes it
                p0.heart.setMaxFrame(80)
                p0.heart.playAnimation()
                p0.counter.text = (postViewModel.postRepo.allPosts.value!![p1].likers.size + 1).toString()
            }
            postViewModel.updateLikers(
                postViewModel.postRepo.allPosts.value!![p1],
                Model.updateLiker(userViewModel.userRepo.user.value!!._id!!)
            )
        }
        if (postViewModel.postRepo.allPosts.value!![p1].post_image_filename != null && postViewModel.postRepo.allPosts.value!![p1].type == PostType.IMAGE.toString()) {
            p0.image.visibility = View.VISIBLE
            Picasso.get().cancelRequest(p0.image)
            Picasso.get().load(SERVER_IMG_URL + postViewModel.postRepo.allPosts.value!![p1].post_image_filename)
                .into(p0.image)
        } else {
            p0.image.visibility = View.GONE
            p0.image.setImageDrawable(null)
        }
    }

    inner class PostViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.post_Title
        val description: TextView = v.post_Description
        val date: TextView = v.post_Date
        val image: ImageView = v.post_image
        val counter: TextView = v.post_likeCounter
        val heart: LottieAnimationView = v.post_heart
    }

}