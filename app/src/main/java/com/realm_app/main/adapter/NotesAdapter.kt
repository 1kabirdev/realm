package com.realm_app.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.realm_app.models.Note
import com.realm_app.databinding.NoteRowBinding

class NotesAdapter(private val onClickListener: OnClickListener, private val onSwipe: OnSwiper) :
    RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {

    private var notesList: MutableList<Note> = mutableListOf()

    fun addItemNotes(notes: MutableList<Note>) {
        notesList = notes
    }

    inner class MyViewHolder(private val binding: NoteRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.titleTextView.text = note.title
            binding.descriptionTextView.text = note.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            NoteRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notesList[position]
        holder.bind(note)
        onSwipe.onClick(note)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(note)
        }
    }

    class OnClickListener(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }

    class OnSwiper(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }

    override fun getItemCount(): Int = notesList.size
}
