package com.uc.machiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.uc.machiapp.controller.InternalFileRepository
import com.uc.machiapp.controller.NoteRepository
import com.uc.machiapp.databinding.ActivityModalBinding
import com.uc.machiapp.model.Note
import com.uc.machiapp.utils.showToast

class ModalActivity : AppCompatActivity() {

    private val repo: NoteRepository by lazy { InternalFileRepository(this) }
    private lateinit var binding: ActivityModalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWrite.setOnClickListener {
            if (binding.edtFileName.text.isNotEmpty()) {
                try {
                    repo.addNote(
                        Note(
                            binding.edtFileName.text.toString(),
                            binding.edtNoteText.text.toString()
                        )
                    )
                } catch (e: Exception) {
                    showToast("File write failed")
                }
                binding.edtFileName.text.clear()
                binding.edtNoteText.text.clear()
            } else {
                showToast("Please provide a filename")
            }
        }

        binding.btnRead.setOnClickListener {
            if (binding.edtFileName.text.isNotEmpty()) {
                try {
                    val note = repo.getNote(binding.edtFileName.text.toString())
                    binding.edtNoteText.setText(note.noteText)
                } catch (e: Exception) {
                    showToast("File read failed")
                }
            } else {
                showToast("Please provide a filename")
            }
        }

        binding.btnDelete.setOnClickListener {
            if (binding.edtFileName.text.isNotEmpty()) {
                try {
                    if (repo.deleteNote(binding.edtFileName.text.toString()))
                        showToast("File deleted")
                    else showToast("File could not be deleted")
                } catch (e: Exception) {
                    showToast("File delete failed")
                }
                binding.edtFileName.text.clear()
                binding.edtNoteText.text.clear()
            } else {
                showToast("Please provide a filename")
            }
        }

    }
}