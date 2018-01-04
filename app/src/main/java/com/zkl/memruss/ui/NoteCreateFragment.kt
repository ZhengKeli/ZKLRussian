package com.zkl.memruss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zkl.memruss.R
import com.zkl.memruss.control.note.NotebookKey
import com.zkl.memruss.core.note.ConflictSolution
import com.zkl.memruss.core.note.NoteContent
import com.zkl.memruss.core.note.QuestionContent
import com.zkl.memruss.core.note.addNote
import kotlinx.android.synthetic.main.fragment_note_create.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.ArrayBlockingQueue

class NoteCreateFragment : NotebookHoldingFragment(),
	NoteConflictDialog.ConflictSolvedListener {
	
	companion object {
		fun newInstance(notebookKey: NotebookKey)
			= NoteCreateFragment::class.java.newInstance(notebookKey)
	}
	
	//view
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
		= inflater.inflate(R.layout.fragment_note_create, container, false)
	
	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		b_mass.setOnClickListener {
			fragmentManager.popBackStack()
			NoteCreateMassFragment.newInstance(notebookKey).jump(fragmentManager)
		}
		
		noteContentEditHolder = null
		updateNoteContent(QuestionContent("", ""))
		
		b_ok.setOnClickListener {
			val content = noteContentEditHolder?.applyChange() ?: return@setOnClickListener
			launch(CommonPool) {
				var committed = true
				mutableNotebook.addNote(content) { conflictNoteId, newContent ->
					val situation = NoteConflictDialog.ConflictSituation(
						true, conflictNoteId, newContent, false)
					launch(UI) {
						NoteConflictDialog.newInstance(notebookKey, situation,
							true, this@NoteCreateFragment).show(fragmentManager)
					}
					val solution = conflictSolutionChan.take()
					if (solution == null) committed = false
					solution ?: ConflictSolution(false, false)
				}
				if (committed) launch(UI) { fragmentManager.popBackStack() }
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
	
	private val conflictSolutionChan = ArrayBlockingQueue<ConflictSolution?>(1)
	override fun onConflictSolved(solution: ConflictSolution?) {
		conflictSolutionChan.put(solution)
	}
	
	//noteContent
	private var noteContentEditHolder: NoteContentEditHolder? = null
	private fun updateNoteContent(noteContent: NoteContent) {
		val oldHolder = noteContentEditHolder
		if (oldHolder?.isCompatible(noteContent) == true) {
			oldHolder.noteContent = noteContent
		} else {
			val holder = noteContent.newEditHolderOrThrow(context,fl_noteContent)
			fl_noteContent.removeAllViews()
			fl_noteContent.addView(holder.view)
			noteContentEditHolder = holder
		}
	}
	
}

