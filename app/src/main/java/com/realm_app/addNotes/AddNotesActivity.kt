package com.realm_app.addNotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.realm_app.addNotes.viewModel.AddMotesViewModel
import com.realm_app.databinding.ActivityAddNotesBinding
import com.realm_app.main.viewModel.MainViewModel

class AddNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var viewModel: AddMotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AddMotesViewModel::class.java]

        with(binding) {
            saveButton.setOnClickListener {
                if (titleEditText.text.toString()
                        .isEmpty() || descriptionEditText.text.toString().isEmpty()
                ) {
                    return@setOnClickListener
                } else {
                    viewModel.addNote(
                        titleEditText.text.toString(),
                        descriptionEditText.text.toString()
                    )
                    finish()
                }
            }
        }
    }
}