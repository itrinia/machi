package com.uc.machiapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uc.machiapp.controller.InternalFileRepository
import com.uc.machiapp.controller.NoteRepository
import com.uc.machiapp.databinding.NoteItemBinding
import com.uc.machiapp.model.Note
import com.uc.machiapp.utils.DATE_FORMAT
import com.uc.machiapp.utils.NO_NOTE_PRIORITY
import com.uc.machiapp.utils.NoteSortOrder
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val context: Context,
    private var priorities: Set<String>,
    private var order: NoteSortOrder,
    private val onNoteClicked: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val repo: NoteRepository by lazy { InternalFileRepository(context) }
    private val simpleDateFormat by lazy { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }
    private val notes: MutableList<Note> = mutableListOf()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        notes.clear()
        notes.addAll(getNotes())
        notifyDataSetChanged()
    }

    private fun getNotes() = repo.getNotesWithPrioritySortedBy(priorities, order)

    fun addNote(note: Note): Boolean {
        val isNoteAdded = repo.addNote(note)

        if (isNoteAdded) loadNotes()

        return isNoteAdded
    }

    fun editNote(note: Note) {
        repo.editNote(note)
        loadNotes()
    }

    fun deleteNote(fileName: String) {
        repo.deleteNote(fileName)
        loadNotes()
    }

    fun updateNotesFilters(priorities: Set<String>? = null, order: NoteSortOrder? = null) {
        if (priorities != null) this.priorities = priorities
        if (order != null) this.order = order

        loadNotes()
    }

    inner class ViewHolder(private val binding: NoteItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(note: Note, onNoteClicked: (Note) -> Unit) {
            val priorityText =
                if (note.priority != 4) note.priority.toString()
                else NO_NOTE_PRIORITY

            binding.txtFileName.text = note.fileName
            binding.txtNoteText.text = note.noteText
            binding.txtPriority.text = priorityText
            binding.txtLastModified.text = simpleDateFormat.format(note.dateModified)
            binding.root.setOnClickListener { onNoteClicked(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(notes[position], onNoteClicked)
    }

    override fun getItemCount(): Int = notes.size
}