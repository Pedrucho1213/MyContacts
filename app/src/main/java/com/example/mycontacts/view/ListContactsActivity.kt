package com.example.mycontacts.view

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontacts.adapters.ContactAdapter
import com.example.mycontacts.databinding.ActivityListContactsBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.viewModel.ContactsViewModel

class ListContactsActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityListContactsBinding
    private lateinit var adapter: ContactAdapter
    private var allContacts = mutableListOf<Contact>()
    private var contacts = mutableListOf<Contact>()
    private val viewModel by lazy { ViewModelProvider(this)[ContactsViewModel::class.java] }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setEvents()
        getAllData()
    }

    override fun onResume() {
        super.onResume()
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
        binding.searchBar.setOnQueryTextListener(this)
        binding.searchBar.setIconifiedByDefault(false)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        val query = binding.searchBar.query
        performSearch(query.toString())
        return true
    }

    private fun filterContacts(query: String) {
        contacts.clear()

        if (query.isEmpty()) {
            contacts.addAll(allContacts)
        } else {
            val filteredContacts = allContacts.filter { contact ->
                contact.name.contains(query, ignoreCase = true) ||
                        contact.number.toString().contains(query, ignoreCase = true)
            }
            contacts.addAll(filteredContacts)
        }

        adapter.notifyDataSetChanged()
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            val filteredContacts = contacts.filter { contact ->
                contact.name.contains(query, ignoreCase = true) ?: false ||
                        contact.maternalSurname.contains(query, ignoreCase = true) ?: false ||
                        contact.paternalSurname.contains(query, ignoreCase = true) ?: false ||
                        contact.number.toString().contains(query, ignoreCase = true) ?: false
            }
            adapter.updateData(filteredContacts)
        } else {
            adapter.updateData(contacts)
        }
    }


}