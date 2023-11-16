package com.example.mycontacts.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycontacts.interfaces.GoogleLoginAction
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthFirebaseViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val googleSignInResult: MutableLiveData<GoogleSignInResult> = MutableLiveData()
    private lateinit var googleLoginAction: GoogleLoginAction

    fun signInWithGoogle() {
        googleLoginAction.performGoogleLogin()
    }

    fun setGoogleLoginAction(googleLoginAction: GoogleLoginAction) {
        this.googleLoginAction = googleLoginAction
    }

    fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            googleSignInResult.value = if (task.isSuccessful) {
                GoogleSignInResult.Success
            } else {
                GoogleSignInResult.Failure
            }
        }
    }

    sealed class GoogleSignInResult {
        object Success : GoogleSignInResult()
        object Failure : GoogleSignInResult()
    }
}