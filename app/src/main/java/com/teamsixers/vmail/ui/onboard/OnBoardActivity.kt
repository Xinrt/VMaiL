package com.teamsixers.vmail.ui.onboard

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.teamsixers.vmail.VMailApplication
import com.teamsixers.vmail.ui.login.LoginActivity
import com.teamsixers.vmail.ui.main.MainActivity

/**
 * OnBoardActivity checks whether the user has logged in,
 * if user has logged in before, it will get the login information from the local storage,
 * if user has not logged in, VMail app will go to LoginActivity
 *
 * @since 2.0
 *
 * @author Mingyan(Cyril) Wang
 */
class OnBoardActivity : AppCompatActivity() {

    /**
     * View model will perform data access task.
     */
    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(OnBoardViewModel::class.java)
    }

    /**
     * OnCreate this activity will
     * check whether the user has logged in,
     * if user has logged in before, App will get the login information from the local storage,
     * if user has not logged in, VMail app will go to LoginActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = window
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag, flag)
        viewModel.getUserConfigResult.observe(this) {
            Log.d("OnBoardActivity", "Config starttls: ${it.isSmtpSTARTTLS}")
            VMailApplication.config = it
            MainActivity.actionStart(this)
            finish()
        }

        viewModel.checkLoginResult.observe(this) {
            if (it == true) {
                viewModel.getUserConfig()
            } else {
                LoginActivity.actionStart(this)
                finish()
            }
        }

        Handler().postDelayed({
            viewModel.checkLogin()
        }, 1000)


    }

}