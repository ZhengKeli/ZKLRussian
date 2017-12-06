package com.zkl.zklRussian.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zkl.zklRussian.R
import com.zkl.zklRussian.control.note.NotebookKey
import com.zkl.zklRussian.core.note.ConflictException
import com.zkl.zklRussian.core.note.NoteContent
import com.zkl.zklRussian.core.note.NoteMemoryState
import com.zkl.zklRussian.core.note.QuestionContent
import kotlinx.android.synthetic.main.fragment_note_edit.*

class NoteEditFragment : NoteHoldingFragment(),
	NoteDeleteDialog.NoteDeletedListener,
	NoteConflictDialog.ConflictSolvedListener {
	
	companion object {
		fun newInstance(notebookKey: NotebookKey, noteId: Long)
			= NoteEditFragment::class.java.newInstance(notebookKey, noteId)
	}
	
	//view
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
		= inflater.inflate(R.layout.fragment_note_edit, container, false)
	
	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (noteId == -1L) initViewsCreateMode()
		else initViewsEditMode()
	}
	
	private fun initViewsEditMode() {
		if (tryLoadNote() == null) {
			fragmentManager.popBackStack()
			return
		}
		
		tv_title.text = getString(R.string.Note_edit_id, noteId)
		b_delete.setOnClickListener {
			NoteDeleteDialog.newInstance(notebookKey, noteId,this).show(fragmentManager)
		}
		
		noteContentEditHolder = null
		updateNoteContent()
		cb_remainProgress.visibility = View.VISIBLE
		
		b_ok.setOnClickListener {
			val newNoteContent = noteContentEditHolder!!.applyChange()
			try {
				mutableNotebook.modifyNoteContent(noteId, newNoteContent)
				if (!cb_remainProgress.isChecked)
					mutableNotebook.modifyNoteMemory(noteId, NoteMemoryState.infantState())
				fragmentManager.popBackStack()
			} catch (e: ConflictException) {
				val modifyRequest = NoteConflictDialog.ModifyRequest(noteId, newNoteContent, cb_remainProgress.isChecked)
				NoteConflictDialog.newInstance(notebookKey, modifyRequest,this).show(fragmentManager)
			}
		}
		b_cancel.setOnClickListener {
			fragmentManager.popBackStack()
		}
	}
	
	private fun initViewsCreateMode() {
		tv_title.text = getString(R.string.Note_create)
		b_delete.visibility = View.GONE
		
		noteContentEditHolder = null
		updateNoteContent(QuestionContent("", ""))
		cb_remainProgress.visibility = View.GONE
		
		b_ok.setOnClickListener {
			val newNoteContent = noteContentEditHolder!!.applyChange()
			try {
				mutableNotebook.addNote(newNoteContent)
				fragmentManager.popBackStack()
			} catch (e: ConflictException) {
				val modifyRequest = NoteConflictDialog.ModifyRequest(-1, newNoteContent, false)
				NoteConflictDialog.newInstance(notebookKey, modifyRequest,this).show(fragmentManager)
			}
		}
		b_cancel.setOnClickListener {
			fragmentManager.popBackStack()
		}
	}
	
	override fun onResume() {
		super.onResume()
		noteContentEditHolder?.requestFocus()
	}
	
	override fun onNoteDeleted() {
		fragmentManager.popBackStack()
	}
	
	override fun onConflictSolved() {
		fragmentManager.popBackStack()
	}
	
	//noteContent
	private var noteContentEditHolder: NoteContentEditHolder? = null
	
	private fun updateNoteContent(noteContent: NoteContent = note.content) {
		val oldHolder = noteContentEditHolder
		if (oldHolder?.isCompatible(noteContent) == true) {
			oldHolder.noteContent = noteContent
		} else {
			val holder = typedNoteContentEditHolders[noteContent.typeTag]?.invoke(activity, fl_noteContent)
				?: throw RuntimeException("The noteContent type \"${noteContent.typeTag}\" is not supported.")
			holder.noteContent = noteContent
			fl_noteContent.removeAllViews()
			fl_noteContent.addView(holder.view)
			noteContentEditHolder = holder
		}
	}
	
}

