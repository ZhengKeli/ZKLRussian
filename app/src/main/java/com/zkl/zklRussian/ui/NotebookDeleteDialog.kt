package com.zkl.zklRussian.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import com.zkl.zklRussian.R
import com.zkl.zklRussian.control.myApp
import com.zkl.zklRussian.control.note.NotebookBrief
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast

class NotebookDeleteDialog : DialogFragment() {
	
	interface NotebookDeletedListener {
		fun onNotebookDeleted()
	}
	
	companion object {
		val arg_notebookBrief = "notebookBrief"
		fun <T> newInstance(notebookBrief: NotebookBrief, deletedListener: T?): NotebookDeleteDialog
			where T : NotebookDeletedListener, T : Fragment
			= NotebookDeleteDialog::class.java.newInstance().apply {
			arguments += bundleOf(NotebookMenuDialog.arg_notebookBrief to notebookBrief)
			setTargetFragment(deletedListener, 0)
		}
	}
	
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(context)
			.setTitle(R.string.notebook_deletion_ConfirmTitle)
			.setMessage(R.string.notebook_deletion_ConfirmMessage)
			.setNegativeButton(R.string.cancel, null)
			.setPositiveButton(R.string.ok) { _, _ ->
				val notebookBrief = arguments.getSerializable(arg_notebookBrief) as NotebookBrief
				if (myApp.notebookShelf.deleteNotebook(notebookBrief.file))
					(targetFragment as? NotebookDeletedListener)?.onNotebookDeleted()
				else toast(getString(R.string.error))
			}
			.create()
	}
	
}


