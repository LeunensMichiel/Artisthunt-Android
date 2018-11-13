package com.leunesmedia.soundhub

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.leunesmedia.soundhub.Adapters.ViewPageAdapter
import com.leunesmedia.soundhub.Fragments.Fragment_NewPost
import com.leunesmedia.soundhub.Fragments.Fragment_Posts
import com.leunesmedia.soundhub.Fragments.Fragment_Profile
import com.leunesmedia.soundhub.Fragments.Fragment_Search

class MainActivity : AppCompatActivity() {
    var tabLayout : TabLayout? = null
    var viewPager : ViewPager? = null
    var adapter : ViewPageAdapter? = null
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        tabLayout = findViewById(R.id.tablelayout_id)
        viewPager = findViewById(R.id.viewpager_id)

        adapter = ViewPageAdapter(supportFragmentManager)

        adapter!!.addFragment(Fragment_Posts(), "")
        adapter!!.addFragment(Fragment_NewPost(), "")
        adapter!!.addFragment(Fragment_Search(), "")
        adapter!!.addFragment(Fragment_Profile(), "")

        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)

        tabLayout!!.getTabAt(0)?.setIcon(R.drawable.ic_face_black)
        tabLayout!!.getTabAt(1)?.setIcon(R.drawable.ic_add_circle)
        tabLayout!!.getTabAt(2)?.setIcon(R.drawable.ic_search)
        tabLayout!!.getTabAt(3)?.setIcon(R.drawable.ic_account_circle)

        supportActionBar!!.elevation = 0F
    }

    override fun onStart() {
        super.onStart()

        var currentUser: FirebaseUser? = mAuth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
