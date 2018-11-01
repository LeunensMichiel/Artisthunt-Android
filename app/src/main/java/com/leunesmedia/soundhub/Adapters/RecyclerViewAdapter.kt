package com.leunesmedia.soundhub.Adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.leunesmedia.soundhub.Domain.Post
import com.leunesmedia.soundhub.R
import com.leunesmedia.soundhub.R.id.tekst
import com.leunesmedia.soundhub.R.id.titel
import kotlinx.android.synthetic.main.item_post.*
import kotlinx.android.synthetic.main.item_post.view.*

class RecyclerViewAdapter(val items: ArrayList<Post>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostViewHolder {
        val vholder = PostViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, p0, false))

        val postDialog = Dialog(context)
        postDialog.setContentView(R.layout.item_post_dialog)
        postDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //oude manier, kreeg anders nullpointerexceptions
        val titel = postDialog.findViewById<TextView>(R.id.dialogtitel)
        val tekst = postDialog.findViewById<TextView>(R.id.dialogtekst)

        vholder.postitem.setOnLongClickListener { v ->
            titel.text = items.get(vholder.adapterPosition).titel
            tekst.text = items.get(vholder.adapterPosition).tekst
            postDialog.show()
            return@setOnLongClickListener true
        }


        return vholder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: PostViewHolder, p1: Int) {
        p0.titel.text = items.get(p1).titel
        p0.tekst.text = items.get(p1).tekst
    }


    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titel = itemView.titel
        val tekst = itemView.tekst
        val postitem = itemView.post_item_id
    }
}