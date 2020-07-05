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
import com.google.firebase.auth.FirebaseAuth
import com.tahhan.filmer.R
import com.tahhan.filmer.utils.HelperFunctions
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment]
 * Used to handle Login process
 */
class LoginFragment : Fragment() {
    companion object {
          val mAuth: FirebaseAuth by lazy {
               FirebaseAuth.getInstance()
          }
    }
    val TAG = LoginFragment::class.java.name

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    /**
     * a function to trigger a user click on the buttons
     * @param view
     * @param savedInstanceState
     * @return does not return anything.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create_account.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginToRegister())
        }
        btn_login.setOnClickListener{
            Log.d(TAG, "onClick: Attempting to login")
            //check for null valued EditText fields
            if (!isEmpty(input_email.text.toString())
                && !isEmpty(input_password.text.toString())
            ) {
                login(input_email.text.toString(), input_password.text.toString())
            } else {
                Toast.makeText(
                    activity,
                    "You must fill out all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }



    /**
     * a function used to login
     * @param email
     * @param password
     * @return does not return anything.
     */
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful && task.isComplete) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser

                    // navigate to popular movies fragment
                    findNavController().navigate(LoginFragmentDirections.actionLoginToPopularMovies())
                    with(MainActivity.sharedPref.edit()) {
                        putBoolean(getString(R.string.login_state_key), true)
                        apply()
                        HelperFunctions.hideKeyboardFrom(requireContext(),requireView())

                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(
                        TAG,
                        "signInWithEmail:failure",
                        task.exception
                    )
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

}