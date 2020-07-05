package com.tahhan.filmer.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class HelperFunctions {
    companion object{
        fun hideKeyboardFrom(context: Context, view: View) {
            val imm: InputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }



    }
}