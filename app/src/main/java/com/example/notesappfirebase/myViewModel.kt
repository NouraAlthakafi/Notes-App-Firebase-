package com.example.notesappfirebase

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class myViewModel(activity: Application): AndroidViewModel(activity){
    private lateinit var notesArray: MutableLiveData<List<Notes>>
    val db = Firebase.firestore
    var TAG = "myMainActivity"
    init{
        notesArray = MutableLiveData()
    }
    fun getNotesList(): LiveData<List<Notes>> {
        return notesArray
    }
    fun getNotes(){
        db.collection("notesArray")
            .get()
            .addOnSuccessListener { result ->

                val allNotes = arrayListOf<Notes>()
                for (document in result) {

                    document.data.map { (key, value) -> allNotes.add(Notes(document.id, value.toString())) }
                }
                notesArray.postValue(allNotes)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }
    fun insertNote(note: Notes){
        GlobalScope.launch(Main) {
            val note = hashMapOf(
                "Note" to note.note
            )
            db.collection("notesArray")
                .add(note)
            getNotes()
        }
    }
    fun updateNote(id: String, newNote: String){
        GlobalScope.launch(Main) {
            db.collection("notesArray")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        if(document.id == id){
                            db.collection("notesArray").document(id).update("Note", newNote)
                        }
                    }
                    getNotes()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error updating documents.", exception)
                }
        }
    }
    fun deleteNote(id: String){
        GlobalScope.launch(Main) {
            db.collection("notesArray")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        if(document.id == id){
                            db.collection("notesArray").document(id).delete()
                        }
                    }
                    getNotes()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error updating documents.", exception)
                }
        }
    }
}
