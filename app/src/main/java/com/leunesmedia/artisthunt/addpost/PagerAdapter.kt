package com.leunesmedia.artisthunt.addpost

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.leunesmedia.artisthunt.domain.PostType

class PagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(p0: Int): Fragment {
        when (p0) {
            0 -> return AddPostImageFragment()
            2 -> return AddPostMusicFragment()
            else -> {
                return AddPostTextFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> PostType.IMAGE.toString()
            2 -> PostType.AUDIO.toString()
            else -> {
                PostType.TEXT.toString()
            }
        }
    }

}