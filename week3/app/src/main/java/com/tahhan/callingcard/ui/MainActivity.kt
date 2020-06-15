package com.tahhan.callingcard.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.tahhan.callingcard.BuildConfig
import com.tahhan.callingcard.R

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(
                    arrayOf("android.permission.CALL_PHONE"),
                    REQUEST_CODE
                )
            }
        }

    }

    /*
    * inflating the menu item we've created into the option menu
    */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }


    // a function to trigger when a specific menu item clicked

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> initDialog()
        }
        return true
    }

    // a function to instantiate an alert dialog
    private fun initDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_info))
        builder.setMessage(
            getString(
                R.string.version_code,
                BuildConfig.VERSION_CODE
            )
        ).show()
    }

}
