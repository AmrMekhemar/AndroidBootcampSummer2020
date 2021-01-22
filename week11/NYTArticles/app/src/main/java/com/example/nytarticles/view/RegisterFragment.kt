package com.example.nytarticles.view

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nytarticles.R
import com.example.nytarticles.networking.NetworkStatusChecker
import com.example.nytarticles.utils.toast
import com.example.nytarticles.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.init.appCtx


class RegisterFragment : Fragment() {
    private val TAG = RegisterFragment::class.java.name
    private val userViewModel: UserViewModel by viewModel()
    private val networkStatusChecker: NetworkStatusChecker by inject()

    /**
     * Inflate the layout for this fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_toLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterToLogin())
        }
        userViewModel.isUserLoggedIn().observe(this.viewLifecycleOwner, Observer { isLoggedIn ->
            if (isLoggedIn)
                findNavController().navigate(RegisterFragmentDirections.actionRegisterToArticles())
        })

        btn_signup.setOnClickListener {
            networkStatusChecker.performIfConnectedToInternet {
                attemptToRegister()
            }
        }
    }

    /**
     * a function registers an accounts if the entries are correct...
     */
    private fun attemptToRegister() {
        Log.d(TAG, "onClick: attempting to register.")
        val shakeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        //check for null valued EditText fields
        if (!isEmpty(input_email.text.toString())
            && !isEmpty(input_password.text.toString())
            && !isEmpty(input_confirmPassword.text.toString())
        ) {
            //check if passwords match
            if (input_password.text.toString() == input_confirmPassword.text.toString()) {
                //check if password is 8 characters or more
                if (input_password.text.length >= 8) {
                    //Initiate registration task
                    userViewModel.register(
                        input_email.text.toString(),
                        input_password.text.toString(),
                        input_name.text.toString()
                    )
                } else {
                    appCtx.toast(getString(R.string.shoudNotBeLessThan8))
                    input_password.startAnimation(shakeAnim)
                }
            } else {
                appCtx.toast(getString(R.string.notMatching))
                input_password.startAnimation(shakeAnim)
                input_confirmPassword.startAnimation(shakeAnim)
            }
        } else {
            appCtx.toast(getString(R.string.fillAllFields))
            shakeAllEditTexts(shakeAnim)
        }
    }

    private fun shakeAllEditTexts(shakeAnim: Animation?) {
        input_password.startAnimation(shakeAnim)
        input_confirmPassword.startAnimation(shakeAnim)
        input_email.startAnimation(shakeAnim)
        input_name.startAnimation(shakeAnim)
    }

}