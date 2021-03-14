package com.example.simplenotes.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.simplenotes.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @ExperimentalStdlibApi
    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment

        if (navHostFragment.childFragmentManager.fragments.isNotEmpty()) {
            val currentFrag = navHostFragment.childFragmentManager.fragments.first()
            if (currentFrag is MainScreenFragment) finish() else super.onBackPressed()
        }
    }
}