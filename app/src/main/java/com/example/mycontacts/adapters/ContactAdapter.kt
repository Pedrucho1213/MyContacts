package com.example.mycontacts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.R
import com.example.mycontacts.model.Contact

class ContactAdapter(private var contact: List<Contact>, private val context: Context) :
    RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int = contact.size
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contact[position]
        holder.bindView(contact, context)
    }

    fun updateData(filteredPosts: List<Contact>) {
        contact = filteredPosts
        notifyDataSetChanged()
    }

}