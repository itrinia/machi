package com.uc.machiapp.controller

import com.uc.machiapp.model.Note
import com.uc.machiapp.utils.NoteSortOrder

interface NoteRepository {
    fun addNote(note: Note): Boolean
    fun getNote(fileName: String): Note
    fun deleteNote(fileName: String): Boolean
    fun editNote(note: Note)
    fun getNotes(): List<Note>
    fun getNotesWithPrioritySortedBy(priorities: Set<String>, order: NoteSortOrder): List<Note>
}