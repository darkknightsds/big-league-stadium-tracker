package com.darkknightsds.bigleaguestadiumtracker

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.darkknightsds.bigleaguestadiumtracker.account.AccountFragment
import com.darkknightsds.bigleaguestadiumtracker.community.CommunityFragment
import com.darkknightsds.bigleaguestadiumtracker.stadiums.StadiumsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val stadiumsFragment = StadiumsFragment()
                loadFragment(stadiumsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val communityFragment = CommunityFragment()
                loadFragment(communityFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val accountFragment = AccountFragment()
                loadFragment(accountFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit()
    }
}
