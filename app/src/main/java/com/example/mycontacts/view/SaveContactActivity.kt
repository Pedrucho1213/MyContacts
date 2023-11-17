package com.example.mycontacts.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ActivitySaveContactBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.viewModel.ContactsViewModel
import com.google.android.material.snackbar.Snackbar

class SaveContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaveContactBinding
    private val viewModel by lazy { ViewModelProvider(this)[ContactsViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setEvents()
    }


    private fun setEvents() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_btn -> validateInput()
                else -> false
            }
        }
    }

    private fun validateInput(): Boolean {
        if (isInputValid()) {
            verifyIfExistInDb()

        } else {
            Snackbar.make(
                binding.root,
                "Fields cannot be empty",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        return true
    }

    private fun verifyIfExistInDb() {
        if (isNetworkAvailable(this)) {
            val number = binding.numberTxt.editText?.text.toString().toInt()
            viewModel.verifyInDB(number).observe(this) { result ->
                if (result) {
                    Snackbar.make(
                        binding.root,
                        "This number is already registered",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    if (isNetworkAvailable(this)) {
                        saveContact()
                    }
                }
            }
        }
    }

    private fun saveContact() {
        val name = binding.nameTxt.editText?.text.toString()
        val maternal = binding.maternalTxt.editText?.text.toString()
        val paternal = binding.paternalTxt.editText?.text.toString()
        val age = binding.ageTxt.editText?.text.toString().toInt()
        val gender = binding.autoCompleteTextView.text.toString()
        val number = binding.numberTxt.editText?.text.toString().toInt()
        val contact = Contact(name, paternal, maternal, age, number, gender, "", "")
        viewModel.saveContactInDb(contact).observe(this) { result ->
            if (result) {
                contactSaved()
            } else {
                errorSavingContact()
            }
        }
    }

    private fun errorSavingContact() {
        Snackbar.make(binding.root, "Has occurred an error, please try later", Snackbar.LENGTH_LONG)
            .show()
    }

    private fun contactSaved() {
        Snackbar.make(binding.root, "The contact has been saved successfully", Snackbar.LENGTH_LONG)
            .show()
        finish()
    }

    private fun isInputValid(): Boolean {
        val ageStr = binding.ageTxt.editText?.text?.toString()
        val numberStr = binding.numberTxt.editText?.text?.toString()

        if (binding.nameTxt.editText?.text.isNullOrBlank() ||
            binding.paternalTxt.editText?.text.isNullOrBlank() ||
            binding.maternalTxt.editText?.text.isNullOrBlank() ||
            ageStr.isNullOrBlank() ||
            numberStr.isNullOrBlank() ||
            binding.autoCompleteTextView.text.isNullOrBlank()) {
            return false
        }

        if (ageStr.toIntOrNull() == null || numberStr.toIntOrNull() == null) {
            return false
        }

        return true
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}

