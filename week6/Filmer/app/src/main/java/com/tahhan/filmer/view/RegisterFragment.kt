package com.tahhan.filmer.view

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.UserProfileChangeRequest
import com.tahhan.filmer.R
import com.tahhan.filmer.view.LoginFragment.Companion.mAuth
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment]
 * to handle registration process.
 */
class RegisterFragment : Fragment() {
    val TAG = RegisterFragment::class.java.name


    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    /**
     * a function to trigger a user click on the buttons
     * @param view
     * @param savedInstanceState
     * @return does not return anything.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_toLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterToLogin())
        }


        btn_signup.setOnClickListener {
            Log.d(TAG, "onClick: attempting to register.")
            //check for null valued EditText fields
            if (!isEmpty(input_email.text.toString())
                && !isEmpty(input_password.text.toString())
                && !isEmpty(input_confirmPassword.text.toString())
            ) {
                //check if passwords match
                if (input_password.text.toString() == input_confirmPassword.text.toString()) {
                    //check if password is 8 characters or more
                    if (input_password.text.length >= 8){
                        //Initiate registration task
                        register(
                            input_email.text.toString(),
                            input_password.text.toString(),
                            input_name.text.toString()
                        )
                    } else {
                        Toast.makeText(activity, getString(R.string.shoudBeLessThan8), Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(activity, getString(R.string.notMatching), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.fillAllFields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * a function used to register an account
     * @param email
     * @param password
     * @param name
     * @return does not return anything.
     */
    private fun register(email: String, password: String, name: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful && task.isComplete) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        activity,
                        getString(R.string.registrationSuccessful),
                        Toast.LENGTH_SHORT
                    ).show()

                    mAuth.currentUser?.updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(name)
                            .build()
                    )

                    findNavController().navigate(RegisterFragmentDirections.actionRegisterToPopularMovies())
                    with(MainActivity.sharedPref.edit()) {
                        putBoolean(getString(R.string.login_state_key), true)
                        commit()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(
                        TAG,
                        "createUserWithEmail:failure",
                        task.exception
                    )
                    Toast.makeText(
                        activity, getString(R.string.registrationFailed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }



}