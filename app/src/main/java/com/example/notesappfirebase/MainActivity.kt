package com.example.notesappfirebase

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    // Declaring ViewModel
    private val myViewModel by lazy { ViewModelProvider(this).get(myViewModel::class.java) }
    // Declaring UI Elements
    lateinit var rvNotes: RecyclerView
    lateinit var notesArray: List<Notes>
    lateinit var etNote: EditText
    lateinit var btnSubmit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initializing ViewModel
        myViewModel.getNotesList().observe(this){
            rvChange(it)
        }
        myViewModel.getNotes()
        // Initializing UI Elements
        rvNotes = findViewById(R.id.rvNotes)
        notesArray = listOf()
        etNote = findViewById(R.id.etNote)
        btnSubmit = findViewById(R.id.btnSubmit)
        // Utilizing
        btnSubmit.setOnClickListener {
            val note = etNote.text.toString()
            if(note.isNotEmpty()){
                myViewModel.insertNote(Notes("", note))
                etNote.text.clear()
                etNote.clearFocus()
            }
            else{
                Toast.makeText(applicationContext, "Please enter something!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun alertUpdate(note: Notes) {
        val builder1 = AlertDialog.Builder(this)
        val noteUpdate = EditText(this)
        noteUpdate.hint = "Update note"
        builder1.setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener { dialog, id ->
                val newNote = noteUpdate.text.toString()
                if(newNote.isNotEmpty()){
                    myViewModel.updateNote(note.id, newNote)
                }
                else{
                    Toast.makeText(applicationContext, "You cannot update it with empty!", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val edit = builder1.create()
        edit.setTitle("Edit")
        edit.setView(noteUpdate)
        edit.show()
    }

    fun delete(id: String) {
        myViewModel.deleteNote(id)
    }

    fun alertDelete(note: Notes){
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage("Are you sure?")
        builder1.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            delete(note.id)
        })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val delete = builder1.create()
        delete.setTitle("Delete")
        delete.show()
    }

    fun rvChange(listN: List<Notes>){
        rvNotes.adapter = RVnotes(this, listN)
        rvNotes.layoutManager = LinearLayoutManager(this)
    }
}