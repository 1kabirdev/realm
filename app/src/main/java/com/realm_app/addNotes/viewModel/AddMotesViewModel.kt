package com.realm_app.addNotes.viewModel

import androidx.lifecycle.ViewModel
import com.realm_app.models.Note
import io.realm.Realm
import java.util.*

class AddMotesViewModel : ViewModel() {

    private var realm: Realm = Realm.getDefaultInstance()

    fun addNote(noteTitle: String, noteDescription: String) {
        realm.executeTransaction { r: Realm ->
            val note = r.createObject(
                Note::class.java, UUID.randomUUID().toString()
            )
            note.title = noteTitle
            note.description = noteDescription
            realm.insertOrUpdate(note)
        }
    }

}