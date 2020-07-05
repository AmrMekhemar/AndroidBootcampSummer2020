package com.tahhan.filmer.view


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.tahhan.filmer.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var sharedPref: SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getPreferences(Context.MODE_PRIVATE)
        val loginState = sharedPref.getBoolean(getString(R.string.login_state_key), false)
        if (savedInstanceState == null) {
            val graph =
                nav_host_fragment.findNavController().navInflater.inflate(R.navigation.nav_graph)
            if (loginState) {
                graph.startDestination = R.id.popularMoviesFragment
            } else {
                graph.startDestination = R.id.loginFragment
            }
            nav_host_fragment.findNavController().graph = graph
        }

    }
}





