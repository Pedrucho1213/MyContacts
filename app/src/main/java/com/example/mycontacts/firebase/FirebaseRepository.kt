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
        session = "pmpedrotorres@gmail.com"
        if (!session.isNullOrBlank()) {
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
        session = "pmpedrotorres@gmail.com"
        if (!session.isNullOrBlank()) {
            val contactsCollection = fireStore.collection("Contacts")
            val query = contactsCollection.whereEqualTo("email", "session")
            query.get()
                .addOnSuccessListener { contact ->
                    val listContacts = mutableListOf<Contact>()
                    for (document in contact) {
                        val name = document.getString("name").toString()
                        val paternal = document.getString("paternalSurname").toString()
                        val maternal = document.getString("maternalSurname").toString()
                        val age = document.getLong("age")?.toInt() // Get 'age' as Long then convert to Int
                        val number = document.getLong("number")?.toInt() // Get 'number' as Long then convert to Int
                        val gender = document.getString("gender").toString()
                        val imageUrl = document.getString("imageUrl").toString()
                        val contacts = age?.let { it1 -> number?.let { it2 ->
                            Contact(name,
                                paternal,
                                maternal,
                                it1,
                                it2,
                                gender,
                                imageUrl,
                                "") } }
                        if (contacts != null) {
                            listContacts.add(contacts)
                        }
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

    fun findContactByNumber(number: Int): LiveData<Contact?> {

        val contactCollection = fireStore.collection("Contacts")

        val contactLiveData = MutableLiveData<Contact?>()

        val query = contactCollection.whereEqualTo("number", number)
        query.get()
            .addOnSuccessListener { result ->
                if(result.documents.size > 0) {
                    val document = result.documents[0]
                    val name = document.getString("name").toString()
                    val paternal = document.getString("paternalSurname").toString()
                    val maternal = document.getString("maternalSurname").toString()
                    val age = document.getLong("age")?.toInt()
                    val number = document.getLong("number")?.toInt()
                    val gender = document.getString("gender").toString()
                    val imageUrl = document.getString("imageUrl").toString()
                    val email = document.getString("email").toString()
                    val contact = age?.let { number?.let {
                        Contact(name, paternal, maternal, it, it, gender, imageUrl, email)}}

                    contactLiveData.value = contact
                }
            }
            .addOnFailureListener {
                contactLiveData.value = null
            }
        return contactLiveData
    }
    fun updateContact(contact: Contact): LiveData<Boolean> {
        val query = fireStore.collection("Contacts")
            .whereEqualTo("number", contact.number)
            //.whereEqualTo("email", session)

        query.get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                success.value = false
            } else {
                val document = result.documents[0]
                document.reference.update(
                    "name", contact.name,
                    "paternalSurname", contact.paternalSurname,
                    "maternalSurname", contact.maternalSurname,
                    "age", contact.age,
                    "number", contact.number,
                    "gender", contact.gender,
                    "imageUrl", contact.imageUrl
                ).addOnSuccessListener {
                    success.value = true
                }.addOnFailureListener {
                    success.value = false
                }
            }
        }.addOnFailureListener {
            success.value = false
        }

        return success
    }

}

