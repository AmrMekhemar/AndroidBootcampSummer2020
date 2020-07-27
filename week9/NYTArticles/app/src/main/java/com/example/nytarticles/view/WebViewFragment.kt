package com.example.nytarticles.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.nytarticles.R
import kotlinx.android.synthetic.main.fragment_web_view.*


/**
 * A simple fragment to receive a url as an argument an display it in a webView
 */
class WebViewFragment : Fragment() {

    /**
     *  Inflate the layout for this fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * if there is a webView state saved in the bundle it's gonna be restored
         */
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            webView.webViewClient = WebViewClient()
            webView.loadUrl(requireArguments()[getString(R.string.url)] as String)
        }

    }

    /**
     * a function to save the state of the webView
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }
}