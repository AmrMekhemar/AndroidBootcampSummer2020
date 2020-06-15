package com.tahhan.callingcard.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tahhan.callingcard.R
import kotlinx.android.synthetic.main.fragment_card.*


// a fragment to view the business card
class CardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
     // inflating the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card, container, false)
    }

    // trigger what happens when the user clicks on the fab
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            makeCall(getString(R.string.phone_number))

        }
    }
    /**
    * a function to make a call
    * @param phoneNumber :String
    * */
    private fun makeCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel: $phoneNumber") //change the number
        startActivity(callIntent)
    }


}