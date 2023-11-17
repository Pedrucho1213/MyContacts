package com.example.mycontacts.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ActivitySaveContactBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.viewModel.ContactsViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.IOException

class SaveContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaveContactBinding
    private val viewModel: ContactsViewModel by lazy { ViewModelProvider(this).get(ContactsViewModel::class.java) }
    private var selectedImageUri: Uri? = null
    private var isEditMode: Boolean = false

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber = intent.getIntExtra("phoneNumber", -1)

        if (phoneNumber != -1) {
            val number = phoneNumber.toInt()
            fetchDataAndDisableFields(number)
            binding.topAppBar.menu.findItem(R.id.save_btn).title = "Edit"
            isEditMode = false
        } else {
            binding.topAppBar.menu.findItem(R.id.save_btn).title = "Save"
            isEditMode = true
        }

        setEvents()
    }

    private fun fetchDataAndDisableFields(number: Int) {
        viewModel.getContactByNumber(number).observe(this) { contact ->
            if (contact != null) {
                binding.nameInput.setText(contact.name)
                binding.paternalInput.setText(contact.paternalSurname)
                binding.maternalInput.setText(contact.maternalSurname)
                binding.ageInput.setText(contact.age.toString())
                binding.numberInput.setText(contact.number.toString())
                binding.autoCompleteTextView.setText(contact.gender, false)
                binding.imageBtn.visibility = View.INVISIBLE

                Glide.with(this)
                    .load(contact.imageUrl)
                    .into(binding.imgView)

                disableInputFields()
            }
        }
    }

    private fun disableInputFields() {
        binding.nameTxt.isEnabled = false
        binding.paternalTxt.isEnabled = false
        binding.maternalTxt.isEnabled = false
        binding.ageTxt.isEnabled = false
        binding.numberTxt.isEnabled = false
        binding.gender.isEnabled = false
    }

    private fun enableInputFields() {
        binding.nameTxt.isEnabled = true
        binding.paternalTxt.isEnabled = true
        binding.maternalTxt.isEnabled = true
        binding.ageTxt.isEnabled = true
        binding.numberTxt.isEnabled = true
        binding.gender.isEnabled = true
    }

    private fun setEvents() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_btn -> {
                    if (isEditMode) {
                        validateInput()
                    } else {
                        enableInputFields()
                        menuItem.title = "Update"
                        isEditMode = !isEditMode
                    }
                     true
                }
                else -> false
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.imageBtn.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select an Option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            captureImage()
        }
    }

    private fun captureImage() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.let {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        binding.indicator.visibility = View.VISIBLE
        if (isInputValid()) {
            verifyIfExistInDb()
        } else {
            Snackbar.make(binding.root, "Fields cannot be empty", Snackbar.LENGTH_SHORT).show()
            binding.indicator.visibility = View.GONE

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
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.indicator.visibility = View.GONE

                } else {
                    if (isNetworkAvailable(this)) {
                        if (selectedImageUri != null) {
                            uploadImageToFirebase(selectedImageUri!!)
                        } else {
                            saveContact()
                        }
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
        val contact = Contact(
            name,
            paternal,
            maternal,
            age,
            number,
            gender,
            selectedImageUri?.toString() ?: "",
            "session"
        )
        viewModel.saveContactInDb(contact).observe(this) { result ->
            if (result) {
                contactSaved()
            } else {
                errorSavingContact()
            }
            binding.indicator.visibility = View.VISIBLE
        }
    }

    private fun errorSavingContact() {
        Snackbar.make(
            binding.root,
            "An error occurred. Please try again later.",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun contactSaved() {
        Snackbar.make(binding.root, "The contact has been saved successfully", Snackbar.LENGTH_LONG)
            .show()
        finish()
    }

    private fun isInputValid(): Boolean {
        val ageStr = binding.ageTxt.editText?.text?.toString()
        val numberStr = binding.numberTxt.editText?.text?.toString()
        return !(binding.nameTxt.editText?.text.isNullOrBlank() ||
                binding.paternalTxt.editText?.text.isNullOrBlank() ||
                binding.maternalTxt.editText?.text.isNullOrBlank() ||
                ageStr.isNullOrBlank() ||
                numberStr.isNullOrBlank() ||
                binding.autoCompleteTextView.text.isNullOrBlank())
    }

    private fun uploadImageToFirebase(uri: Uri) {
        if (isNetworkAvailable(this)) {
            viewModel.saveImage(uri).observe(this) { result ->
                if (!result.toString().isNullOrBlank()) {
                    selectedImageUri = result
                }
                saveContact()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImageUri = getImageUriFromBitmap(this, imageBitmap)
                    binding.imgView.setImageBitmap(getBitmapFromUri(selectedImageUri!!))
                }

                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        selectedImageUri = imageUri
                        binding.imgView.setImageBitmap(getBitmapFromUri(selectedImageUri!!))
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}







