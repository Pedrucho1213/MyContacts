package com.example.mycontacts.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ActivityLoginBinding
import com.example.mycontacts.interfaces.GoogleLoginAction
import com.example.mycontacts.localStorage.PreferencesManager
import com.example.mycontacts.viewModel.AuthFirebaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity(), GoogleLoginAction {
    private val authViewModel: AuthFirebaseViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authViewModel.setGoogleLoginAction(this)
        setEvents()
        observeGoogleSignInResult()
        PreferencesManager.setLogIn(this, true)
        verifyLogin()
    }

    private fun setEvents() {
        binding.googleBtn.setOnClickListener {
            authViewModel.signInWithGoogle()
        }
        binding.rootLayout.setOnClickListener {
            hideKeyboard()
            binding.emailTxt.editText?.clearFocus()
            binding.passTxt.editText?.clearFocus()
            currentFocus?.clearFocus()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
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

    private fun observeGoogleSignInResult() {
        authViewModel.googleSignInResult.observe(this) { result ->
            when (result) {
                AuthFirebaseViewModel.GoogleSignInResult.Success -> {
                    showSnackBar("Login success")
                    PreferencesManager.setLogIn(this, true)
                }

                AuthFirebaseViewModel.GoogleSignInResult.Failure -> {
                    PreferencesManager.setLogIn(this, false)
                    showSnackBar("Login failed")
                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                authViewModel.handleGoogleSignInResult(account)
            } catch (e: ApiException) {
                // Sign in was unsuccessful
            }
        }
    }

    override fun performGoogleLogin() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(R.string.default_web_client_id.toString())
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

}