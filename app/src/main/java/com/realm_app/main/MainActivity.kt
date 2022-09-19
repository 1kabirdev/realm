package com.realm_app.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.realm_app.addNotes.AddNotesActivity
import com.realm_app.main.viewModel.MainViewModel
import com.realm_app.R
import com.realm_app.main.adapter.NotesAdapter
import com.realm_app.databinding.ActivityMainBinding
import com.realm_app.models.Note

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var viewModel: MainViewModel

    private var id: String = ""

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        notesAdapter = NotesAdapter(NotesAdapter.OnClickListener { note ->
            createUpdateDialog(note)
        }, NotesAdapter.OnSwiper {
            id = it.id
        })

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteNote(id)
                Toast.makeText(this@MainActivity, "Note Deleted Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecyclerview)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddNotesActivity::class.java))
        }

        binding.floatingActionButtonDelAll.setOnClickListener {
            createDeleteAllDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createUpdateDialog(note: Note) {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.update_dialog, viewGroup, false)
        val builder = AlertDialog.Builder(this)

        val titleEditText: EditText = dialogView.findViewById(R.id.titleEditText_update)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.descriptionEditText_update)

        titleEditText.setText(note.title)
        descriptionEditText.setText(note.description)

        builder.setView(dialogView)
        builder.setTitle("Update Note")
        builder.setPositiveButton("Update") { _, _ ->
            viewModel.updateNote(
                note.id,
                titleEditText.text.toString(),
                descriptionEditText.text.toString()
            )
            notesAdapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(this@MainActivity, "Canceled Update", Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createDeleteAllDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete all notes")
        builder.setPositiveButton("Delete all") { _, _ ->
            viewModel.deleteAllNotes()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(this@MainActivity, "Canceled Update", Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.allNotes.observe(this) { allNotes ->
            notesAdapter.addItemNotes(allNotes)
            binding.notesRecyclerview.adapter = notesAdapter
        }
    }
}