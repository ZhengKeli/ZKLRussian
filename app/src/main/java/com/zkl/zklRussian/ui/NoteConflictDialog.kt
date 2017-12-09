package com.zkl.zklRussian.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.zkl.zklRussian.R
import com.zkl.zklRussian.control.note.NotebookKey
import com.zkl.zklRussian.core.note.Note
import com.zkl.zklRussian.core.note.NoteContent
import com.zkl.zklRussian.core.note.NoteMemoryState
import com.zkl.zklRussian.core.note.NoteMemoryStatus
import kotlinx.android.synthetic.main.dialog_note_conflict.view.*
import org.jetbrains.anko.bundleOf
import java.io.Serializable

class NoteConflictDialog : NotebookHoldingDialog() {
	
	data class ModifyRequest(val targetNoteId: Long, val noteContent: NoteContent, val remainProgress: Boolean) : Serializable
	
	interface ConflictSolvedListener {
		fun onConflictSolved()
	}
	
	companion object {
		private val arg_modifyRequest = "modifyRequest"
		fun <T>newInstance(notebookKey: NotebookKey, modifyRequest: ModifyRequest, solvedListener: T)
			where T: ConflictSolvedListener, T:Fragment
			= NoteConflictDialog::class.java.newInstance(notebookKey).apply {
			arguments += bundleOf(arg_modifyRequest to modifyRequest)
			setTargetFragment(solvedListener, 0)
		}
		
		val NoteConflictDialog.modifyRequest get() = arguments.getSerializable(arg_modifyRequest) as ModifyRequest
	}
	
	override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
		
		//prepare data
		val request = modifyRequest
		val conflictNotes = getConflictNotes(request.targetNoteId, request.noteContent)
		
		//prepare views
		val view = View.inflate(context, R.layout.dialog_note_conflict, null)
		val dialogBuilder = AlertDialog.Builder(context).setView(view)
		
		dialogBuilder.setTitle(R.string.there_are_conflicted_notes)
		view.lv_conflict.adapter = object : NoteListAdapter() {
			override fun getCount(): Int = conflictNotes.size
			override fun getItem(position: Int): Note = conflictNotes[position]
			override val context: Context get() = activity
		}
		if (conflictNotes.size == 1) {
			val conflictNote = conflictNotes.first()
			if (request.targetNoteId == -1L) {
				// create
				if (conflictNote.memoryState.status != NoteMemoryStatus.infant)
					view.cb_remainProgress.visibility = View.VISIBLE
				
				dialogBuilder.setPositiveButton(R.string.override){ _, _ ->
					mutableNotebook.modifyNoteContent(conflictNote.id, request.noteContent)
					if (!view.cb_remainProgress.isChecked)
						mutableNotebook.modifyNoteMemory(conflictNote.id, NoteMemoryState.infantState())
					(targetFragment as? ConflictSolvedListener)?.onConflictSolved()
				}
				dialogBuilder.setNegativeButton(R.string.cancel,null)
				
			} else {
				//modify
				dialogBuilder.setPositiveButton(R.string.override) { _, _ ->
					mutableNotebook.deleteNote(conflictNote.id)
					mutableNotebook.modifyNoteContent(request.targetNoteId, request.noteContent)
					if (!request.remainProgress)
						mutableNotebook.modifyNoteMemory(request.targetNoteId, NoteMemoryState.infantState())
					(targetFragment as? ConflictSolvedListener)?.onConflictSolved()
				}
			}
		}
		
		return dialogBuilder.create()
	}
	
	private fun getConflictNotes(targetNoteId: Long, noteContent: NoteContent)
		= noteContent.uniqueTags.mapNotNull { uniqueTag ->
		val noteId = notebook.checkUniqueTag(uniqueTag, targetNoteId)
		if (noteId != -1L) notebook.getNote(noteId) else null
	}
	
}

