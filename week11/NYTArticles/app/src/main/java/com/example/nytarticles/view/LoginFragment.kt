package com.example.nytarticles.view

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nytarticles.R
import com.example.nytarticles.networking.NetworkStatusChecker
import com.example.nytarticles.utils.toast
import com.example.nytarticles.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.init.appCtx


class LoginFragment : Fragment() {
    private val TAG = LoginFragment::class.java.name
    private val userViewModel: UserViewModel by viewModel()
    private val networkStatusChecker: NetworkStatusChecker by inject()

    /**
     *  Inflate the layout for this fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create_account.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginToRegister())
        }

        userViewModel.isUserLoggedIn().observe(this.viewLifecycleOwner, Observer { isLoggedIn ->
            if (isLoggedIn)
                findNavController().navigate(LoginFragmentDirections.actionLoginToArticles())
        })

        btn_login.setOnClickListener {
             networkStatusChecker.performIfConnectedToInternet {
                 attemptToLogin()
             }
        }
    }

    /**
     * a function to login a user if the entries are correct
     */
    private fun attemptToLogin() {
        Log.d(TAG, "onClick: Attempting to login")
        //check for null valued EditText fields
        if (!isEmpty(input_email.text.toString())
            && !isEmpty(input_password.text.toString())
        ) {
            userViewModel.login(input_email.text.toString(), input_password.text.toString())
        } else {
            shakeEditTexts()
            appCtx.toast(getString(R.string.must_fill_all_fields))
        }
    }

    private fun shakeEditTexts() {
        val shakeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        input_email.startAnimation(shakeAnim)
        input_password.startAnimation(shakeAnim)
    }

}