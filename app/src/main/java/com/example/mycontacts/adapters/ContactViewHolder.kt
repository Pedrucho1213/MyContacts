package com.example.mycontacts.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.databinding.CardContactBinding
import com.example.mycontacts.model.Contact

class ContactViewHolder(view:View): RecyclerView.ViewHolder(view) {
    private var binding = CardContactBinding.bind(view)
    fun bindView(contact: Contact) {
        binding.nameContactTxt.text = "${contact.name} ${contact.paternalSurname} ${contact.maternalSurname}"
        binding.numberContactTxt.text = contact.number.toString()
    }
}