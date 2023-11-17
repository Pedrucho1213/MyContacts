package com.example.mycontacts.adapters

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.databinding.CardContactBinding
import com.example.mycontacts.model.Contact
import com.example.mycontacts.view.SaveContactActivity
import com.example.mycontacts.viewModel.ContactsViewModel

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding: CardContactBinding = CardContactBinding.bind(itemView)


    fun bind(contact: Contact, context: Context) {

        val fullName = "%s %s %s".format(contact.name, contact.paternalSurname, contact.maternalSurname)
        binding.nameContactTxt.text = fullName
        binding.numberContactTxt.text = contact.number.toString()

        loadContactImage(contact.imageUrl, context)

        itemView.setOnClickListener {
            val intent = Intent(context, SaveContactActivity::class.java)
            intent.putExtra("phoneNumber", contact.number)
            context.startActivity(intent)
        }
    }

    private fun loadContactImage(imageUrl: String, context: Context) {
        val placeholderResId = R.drawable.ic_account_circle_24

        val glideRequest = Glide.with(context)
            .load(if (imageUrl.isBlank()) placeholderResId else imageUrl)

        glideRequest.into(binding.imageContactImg)
    }
}