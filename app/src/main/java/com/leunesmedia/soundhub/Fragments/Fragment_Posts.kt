package com.leunesmedia.soundhub.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.leunesmedia.soundhub.Adapters.RecyclerViewAdapter
import com.leunesmedia.soundhub.Domain.Post
import com.leunesmedia.soundhub.R

class Fragment_Posts : Fragment() {
    var posts : ArrayList<Post> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.posts_fragment, container, false)
        val recview = view.findViewById(R.id.posts_recyclerview) as RecyclerView
        val adapter = RecyclerViewAdapter(posts, activity as Context)
        recview.layoutManager = LinearLayoutManager(activity)
        recview.adapter = adapter
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPostsToPosts()

    }

    private fun addPostsToPosts() {

    }
}