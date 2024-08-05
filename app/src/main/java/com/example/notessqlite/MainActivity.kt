package com.example.notessqlite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), EmptyStateHandler {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNotes(), this, this, db) // Pass the database helper

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val notes = db.getAllNotes()
        notesAdapter.refreshData(notes)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        val notes = db.getAllNotes()
        if (notes.isEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
        }
    }

    override fun showEmptyState() {
        binding.emptyImageView.visibility = View.VISIBLE
    }

    override fun hideEmptyState() {
        binding.emptyImageView.visibility = View.GONE
    }
}
