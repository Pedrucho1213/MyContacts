package com.example.mycontacts.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mycontacts.databinding.ActivityListContactsBinding

class ListContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListContactsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setEvents()
    }

    private fun setEvents() {
        binding.newContactBtn.setOnClickListener {
            val intent = Intent(this, SaveContactActivity::class.java)
            startActivity(intent)
        }
    }
}