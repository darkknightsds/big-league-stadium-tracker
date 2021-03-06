package com.darkknightsds.bigleaguestadiumtracker

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.darkknightsds.bigleaguestadiumtracker.profile.ProfileFragment
import com.darkknightsds.bigleaguestadiumtracker.community.CommunityFragment
import com.darkknightsds.bigleaguestadiumtracker.stadiums.StadiumsFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //Values
    private val TAG: String = "MainActivity"
    private val stadiumsFragment = StadiumsFragment()
    private val communityFragment = CommunityFragment()
    private val accountFragment = ProfileFragment()
    //Variables
    private lateinit var auth: FirebaseAuth

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                loadFragment(stadiumsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                loadFragment(communityFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                loadFragment(accountFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val loginFragment = LoginFragment()
            loadFragment(loginFragment)
        } else {
            loadFragment(stadiumsFragment)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit()
    }
}
