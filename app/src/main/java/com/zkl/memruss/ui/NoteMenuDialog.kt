package com.zkl.memruss.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.zkl.memruss.R
import com.zkl.memruss.control.note.NotebookKey

class NoteMenuDialog : DialogFragment(),
	NoteDeleteDialog.NoteDeletedListener {
	
	interface NoteListChangedListener {
		fun onNoteListChanged()
	}
	
	companion object {
		fun <T> newInstance(notebookKey: NotebookKey, noteId: Long, changedListener: T?)
			where T : NoteListChangedListener, T : Fragment = NoteMenuDialog::class.java.newInstance(notebookKey, noteId).apply {
			setTargetFragment(changedListener, 0)
		}
	}
	
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		
		val notebookKey = argNotebookKey
		val noteId = argNoteId
		
		val itemPairs = arrayListOf(
			getString(R.string.view) to {
				NoteViewFragment.newInstance(notebookKey, noteId).jump(fragmentManager)
			}
		)
		if (notebookKey.mutable) itemPairs.addAll(arrayOf(
			getString(R.string.edit) to {
				NoteEditFragment.newInstance(notebookKey, noteId).jump(fragmentManager)
			},
			getString(R.string.delete) to {
				NoteDeleteDialog.newInstance(notebookKey, noteId, this).show(fragmentManager)
			}
		))
		
		val itemNames = itemPairs.map { it.first }.toTypedArray<String>()
		val itemOperations = itemPairs.map { it.second }.toTypedArray<() -> Unit>()
		
		return AlertDialog.Builder(activity)
			.setItems(itemNames) { _, which ->
				itemOperations[which].invoke()
			}
			.create()
	}
	
	override fun onNoteDeleted() {
		(targetFragment as? NoteListChangedListener)?.onNoteListChanged()
	}
	
}