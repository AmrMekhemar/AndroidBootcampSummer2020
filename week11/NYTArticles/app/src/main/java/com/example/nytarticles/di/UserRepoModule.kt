package com.example.nytarticles.di
import com.example.nytarticles.prefs.SharedPrefsManager
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val userRepoModule = module {
    single { SharedPrefsManager() }
    single { FirebaseAuth.getInstance() }
}