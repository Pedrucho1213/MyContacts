package com.example.mycontacts.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycontacts.firebase.FirebaseRepository
import com.example.mycontacts.model.Contact

class ContactsViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    private val existInDB = MutableLiveData<Boolean>()
    private val savedInDB = MutableLiveData<Boolean>()
    private val mutableData = MutableLiveData<MutableList<Contact>>()
    private val mutableContact = MutableLiveData<Contact>()
    private val savedImage = MutableLiveData<Uri>()
    private val updated = MutableLiveData<Boolean>()
    private val deleted = MutableLiveData<Boolean>()


    fun verifyInDB(number: Int): MutableLiveData<Boolean> {
        repository.checkContactExists(number).observeForever {
            existInDB.value = it
        }
        return existInDB
    }

    fun saveContactInDb(contact: Contact): MutableLiveData<Boolean> {
        repository.saveContactToFireStore(contact).observeForever {
            savedInDB.value = it
        }
        return savedInDB
    }

    fun saveFavorites(contact: Contact): MutableLiveData<Boolean>{
        repository.saveFavorites(contact).observeForever {
            savedInDB.value = it
        }
        return savedInDB
    }

    fun fetchContacts(route:String): LiveData<MutableList<Contact>> {
        repository.getContactsByUserEmail(route).observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun saveImage(uri: Uri): MutableLiveData<Uri>{
        repository.saveImageToFirebaseStorage(uri).addOnSuccessListener {
            savedImage.value = it
        }
        return savedImage
    }

    fun getContactByNumber(number: Int): MutableLiveData<Contact>{
        repository.findContactByNumber(number).observeForever {
            mutableContact.value = it
        }
        return mutableContact
    }

    fun updateContact(contact: Contact): MutableLiveData<Boolean>{
        repository.updateContact(contact).observeForever {
            updated.value = it
        }
        return updated
    }

    fun deleteContact(number: Int): LiveData<Boolean>{
        repository.deleteContact(number).observeForever {
            deleted.value = it
        }
        return deleted
    }

}