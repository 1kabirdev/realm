package com.realm_app.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.realm_app.models.Note
import io.realm.Realm
import io.realm.RealmObject.deleteFromRealm
import io.realm.kotlin.deleteFromRealm

class MainViewModel : ViewModel() {


    val allNotes: LiveData<MutableList<Note>> get() = getAllNotes()

    private fun getAllNotes(): MutableLiveData<MutableList<Note>> {
        val list = MutableLiveData<MutableList<Note>>()
        val notes = realm.where(Note::class.java).findAll()
        list.value = notes?.subList(0, notes.size)
        return list
    }




    fun updateNote(id: String, noteTitle: String, noteDesc: String) {
        val target = realm.where(Note::class.java)
            .equalTo("id", id)
            .findFirst()

        realm.executeTransaction {
            target?.title = noteTitle
            target?.description = noteDesc
            target?.let { update -> realm.insertOrUpdate(update) }
        }
    }






                        private var realm: Realm = Realm.getDefaultInstance()

                        fun deleteNote(id: String) {
                            val notes = realm.where(Note::class.java)
                                .equalTo("id", id)
                                .findFirst()

                            realm.executeTransaction {
                                notes?.deleteFromRealm()
                            }
                        }








    fun deleteAllNotes() {
        val notes = realm.where(Note::class.java).findAll()
        realm.executeTransaction {
            notes?.deleteAllFromRealm()
        }
    }
}