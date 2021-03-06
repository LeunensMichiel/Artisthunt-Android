package com.leunesmedia.artisthunt.addpost

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leunesmedia.artisthunt.R
import kotlinx.android.synthetic.main.fragment_add_post.*

/**
 * Parentfragment that contains the different types of addposts fragments (audio, text, image)
 */
class AddPostFragment : Fragment() {

    /**
     * Creates View
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    /**
     * Initializes ViewPager and sets the default to addText Fragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addPostAdapter = PagerAdapter(childFragmentManager)
        addPostViewPager.adapter = addPostAdapter
        addPostViewPager.currentItem = 1
        addPostTabsLayout.setupWithViewPager(addPostViewPager)
    }
}
