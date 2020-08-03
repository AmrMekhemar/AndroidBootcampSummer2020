package com.example.nytarticles.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.nytarticles.prefs.SharedPrefsManager
import com.example.nytarticles.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import org.koin.core.KoinComponent
import org.koin.core.inject
import splitties.init.appCtx

class UserRepository() : KoinComponent {
    private val sharedPrefsManager: SharedPrefsManager by inject()
    private val TAG: String = UserRepository::class.java.name
    private val mAuth: FirebaseAuth by inject()
    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun getUserName(): String = mAuth.currentUser?.displayName.toString()
    fun getUserEmail(): String = mAuth.currentUser?.email.toString()

    private fun setUserLoggedIn(isLoggedIn: Boolean) {
        sharedPrefsManager.setUserLoggedIn(isLoggedIn)
        this.isLoggedIn.postValue(isLoggedIn)
    }

    fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.isComplete) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    setUserLoggedIn(true)
                } else {
                    setUserLoggedIn(false)
                    appCtx.toast("email or password is incorrect")
                }
            }
    }

    fun register(email: String, password: String, name: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful && task.isComplete) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    mAuth.currentUser?.updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(name)
                            .build()
                    )
                    setUserLoggedIn(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(
                        TAG,
                        "createUserWithEmail:failure",
                        task.exception
                    )
                    appCtx.toast("Registration Error")
                    setUserLoggedIn(false)
                }
            }
    }

    fun logout() {
        mAuth.signOut()
        setUserLoggedIn(false)
    }
}