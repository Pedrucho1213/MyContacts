package com.example.mycontacts.adapters

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.CardContactBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.view.SaveContactActivity

class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var binding = CardContactBinding.bind(view)
    fun bindView(contact: Contact, context: Context) {
        binding.nameContactTxt.text =
            "${contact.name} ${contact.paternalSurname} ${contact.maternalSurname}"
        binding.numberContactTxt.text = contact.number.toString()

        if (contact.imageUrl.isBlank()) {
            Glide.with(context)
                .load(R.drawable.ic_account_circle_24)
                .into(binding.imageContactImg)
        } else {
            Glide.with(context)
                .load(contact.imageUrl)
                .into(binding.imageContactImg)
        }

        itemView.setOnClickListener {
            val intent = Intent(context, SaveContactActivity::class.java)
            intent.putExtra("phoneNumber", contact.number)
            context.startActivity(intent)
        }

    }
}