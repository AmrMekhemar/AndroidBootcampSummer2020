package com.example.nytarticles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nytarticles.repository.UserRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserViewModel : ViewModel(), KoinComponent {
    private val userRepository: UserRepository by inject()
    fun isUserLoggedIn() :LiveData<Boolean> = userRepository.isLoggedIn
    fun getUserName(): String = userRepository.getUserName()
    fun getUserEmail(): String = userRepository.getUserEmail()
    fun login(email: String, password: String) {
         userRepository.login(email, password)
    }

    fun register(email: String, password: String, name: String)  {
         userRepository.register(email, password, name)
    }

    fun logout() {
        userRepository.logout()
    }
}