package com.couleurwestit.emecard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.couleurwestit.emecard.data.model.DBUserInfo
import com.couleurwestit.emecard.ui.infos.MenuFragment
import com.couleurwestit.emecard.ui.infos.UserInfoFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    protected lateinit var fram: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ctx = this.applicationContext

        if (DBUserInfo(ctx, null).findVCard() == null)
            showFragment(UserInfoFragment())
        else
            showFragment(MenuFragment())
        val closerButton = findViewById<FloatingActionButton>(R.id.closer)

        closerButton.setOnClickListener {
            //loadingProgressBar.visibility = View.VISIBLE
            showFragment(MenuFragment())
        }
    }

    fun showFragment(fragment: Fragment) {
        fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main, fragment)
        fram.commit()
    }
}