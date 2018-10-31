package com.leunesmedia.soundhub.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.leunesmedia.soundhub.Domain.Post
import com.leunesmedia.soundhub.R
import kotlinx.android.synthetic.main.item_post.view.*

class RecyclerViewAdapter(val items : ArrayList<Post>, val context : Context) : RecyclerView.Adapter<RecyclerViewAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, p0, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: PostViewHolder, p1: Int) {
        p0.titel.text = items.get(p1).titel
        p0.tekst.text = items.get(p1).tekst
    }


    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titel = itemView.findViewById<TextView>(R.id.titel)
        val tekst = itemView.findViewById<TextView>(R.id.tekst)
    }
}