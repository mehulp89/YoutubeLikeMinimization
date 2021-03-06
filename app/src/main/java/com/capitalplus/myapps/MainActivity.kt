package com.capitalplus.myapps

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * Created by Mehul Patel
 * @mehulp89
 * on 13-04-2019.
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var fragment: Fragment? = null
    val url ="http://techslides.com/demos/sample-videos/small.mp4"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        fragment = FragmentFour()
        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment!!)
            .commit()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_video -> {
                fragment = FragmentFour()
            }
            R.id.nav_text -> {
                fragment = FragmentTwo()
            }
            R.id.nav_text_two -> {
                fragment = FragmentThree()
            }
        }

        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment!!)
            .commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
