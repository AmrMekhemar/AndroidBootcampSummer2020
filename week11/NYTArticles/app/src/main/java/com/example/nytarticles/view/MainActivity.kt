package com.example.nytarticles.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.nytarticles.R
import com.example.nytarticles.prefs.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val sharedPrefsManager: SharedPrefsManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val graph =
                nav_host_fragment.findNavController().navInflater.inflate(R.navigation.nav_graph)
            graph.startDestination = if (sharedPrefsManager.isUserLoggedIn()) {
                R.id.articlesFragment
            } else {
                R.id.loginFragment
            }
            nav_host_fragment.findNavController().graph = graph
        }

    }
}