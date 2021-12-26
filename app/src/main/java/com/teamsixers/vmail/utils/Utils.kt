package com.teamsixers.vmail.utils

import android.widget.Toast
import com.teamsixers.vmail.VMailApplication

/**
 * General Utils for VMail App
 */
object Utils {
    /**
     * Show toast in current application.
     *
     * @param text the text to show.
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    fun toast(text: String) {
        Toast.makeText(VMailApplication.context, text, Toast.LENGTH_SHORT).show()
    }
}