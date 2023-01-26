package com.snappy.socialx.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.snappy.socialx.ui.adapter.FragmentAdapter
import com.snappy.socialx.ui.fragments.LogInFragment
import com.snappy.socialx.R
import com.snappy.socialx.ui.fragments.SignUpFragment

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(LogInFragment(), "LOGIN")
        fragmentAdapter.addFragment(SignUpFragment(), "SIGN UP")

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null){
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
    }
}