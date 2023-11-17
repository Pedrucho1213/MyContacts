package com.example.mycontacts.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mycontacts.R
import com.example.mycontacts.localStorage.PreferencesManager

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        PreferencesManager.setLogIn(this,true)
        verifyLogin()
    }

    private fun verifyLogin() {
        val isLoggedIn = PreferencesManager.getLogIn(this)
        if (isLoggedIn) {
            redirectToHomePage()
        }
    }

    private fun redirectToHomePage() {
        val welcomeMessage = getString(R.string.welcome_message, PreferencesManager.getName(this))
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show()
        val intent = Intent(applicationContext, ListContactsActivity::class.java)
        startActivity(intent)
        finish()
    }
}