package com.example.mycontacts.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontacts.adapters.ContactAdapter
import com.example.mycontacts.databinding.ActivityListContactsBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.viewModel.ContactsViewModel

class ListContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListContactsBinding
    private lateinit var adapter: ContactAdapter
    private var contacts = mutableListOf<Contact>()
    private val viewModel by lazy { ViewModelProvider(this)[ContactsViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setEvents()
        getAllData()
    }

    private fun getAllData() {
        contacts.clear()
        viewModel.fetchContacts().observe(this){
            contacts = it.toMutableList()
            initRecyclerView()
        }

    }

    private fun initRecyclerView(){
        adapter = ContactAdapter(contacts, this)
        binding.contactsRv.layoutManager = LinearLayoutManager(this)
        binding.contactsRv.adapter = adapter
    }

    private fun setEvents() {
        binding.newContactBtn.setOnClickListener {
            val intent = Intent(this, SaveContactActivity::class.java)
            startActivity(intent)
        }
    }


}