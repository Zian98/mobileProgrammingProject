package com.example.notessqlite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(
    private var notes: List<Note>,
    private val context: Context,
    private val emptyStateHandler: EmptyStateHandler,
    private val db: NotesDatabaseHelper // Pass the database helper
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.updateButton.setOnClickListener {
            val intent = Intent(context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(note)
        }
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(context)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog, _ ->
                db.deleteNote(note.id)
                val newNotes = db.getAllNotes()
                refreshData(newNotes)
                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                if (newNotes.isEmpty()) {
                    emptyStateHandler.showEmptyState()
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
        if (notes.isEmpty()) {
            emptyStateHandler.showEmptyState()
        } else {
            emptyStateHandler.hideEmptyState()
        }
    }
}
