package com.example.mycontacts.firebase

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mycontacts.model.Contact
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class FirebaseRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private val success = MutableLiveData<Boolean>()
    private val exist = MutableLiveData<Boolean>()
    private var session = auth.currentUser?.email
    private val contactData = MutableLiveData<MutableList<Contact>>()


    fun saveContactToFireStore(contact: Contact): LiveData<Boolean> {
        if (session != null) {
            val contactCollection = fireStore.collection("Contacts")
            contactCollection.add(contact)
                .addOnSuccessListener {
                    success.value = true
                }
                .addOnFailureListener {
                    success.value = false
                }
        }
        return success
    }

    fun getContactsByUserEmail(): LiveData<MutableList<Contact>> {
        if (session != null) {
            val contactsCollection = fireStore.collection("Contacts")
            val query = contactsCollection.whereEqualTo("email", session)
            query.get()
                .addOnSuccessListener { contact ->
                    val listContacts = mutableListOf<Contact>()
                    for (document in contact) {
                        val name = document.getString("name").toString()
                        val paternal = document.getString("paternalSurname").toString()
                        val maternal = document.getString("maternalSurname").toString()
                        val age = document.getString("age").toString().toInt()
                        val number = document.getString("number").toString().toInt()
                        val gender = document.getString("gender").toString()
                        val imageUrl = document.getString("imageUrl").toString()
                        val contacts = Contact(
                            name,
                            paternal,
                            maternal,
                            age,
                            number,
                            gender,
                            imageUrl,
                            "session"
                        )
                        listContacts.add(contacts)
                    }
                    contactData.value = listContacts
                }
        }
        return contactData
    }

    fun checkContactExists(number: Int): LiveData<Boolean> {
        session = "pmpedrotorres@gmail.com"
        if (!session.isNullOrBlank()) {
            val contactCollection = fireStore.collection("Contacts")
            val query = contactCollection.whereEqualTo("number", number)
                .whereEqualTo("email", "session")
            val result = query.get()
            exist.value = result.isSuccessful
        }
        return exist
    }

    fun saveImageToFirebaseStorage(imageUri: Uri): Task<Uri> {
        val storageRef: StorageReference = storage.reference
        val imageRef: StorageReference = storageRef.child("images/${UUID.randomUUID()}")

        return imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }
    }


}