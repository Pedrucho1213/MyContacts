package com.example.mycontacts.viewModel

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

    fun fetchContacts(): LiveData<MutableList<Contact>> {
        repository.getContactsByUserEmail().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

}