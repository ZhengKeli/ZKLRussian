package com.zkl.zklRussian.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.zkl.zklRussian.R
import com.zkl.zklRussian.control.note.NotebookKey
import com.zkl.zklRussian.core.note.NoteContent
import com.zkl.zklRussian.core.note.QuestionContent

class NoteEditFragment : NoteHoldingFragment {
	
	constructor():super()
	constructor(notebookKey: NotebookKey, noteId: Long) : super(notebookKey, noteId)
	private val isCreateMode get() = noteId==-1L
	
	//view
	private lateinit var tv_title: TextView
	private lateinit var b_delete: Button
	private lateinit var b_ok: Button
	private lateinit var b_cancel: Button
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
		= inflater.inflate(R.layout.fragment_note_edit, container, false).apply {
		
		tv_title = findViewById(R.id.tv_title) as TextView
		b_delete = findViewById(R.id.b_delete) as Button
		
		b_ok = findViewById(R.id.b_ok) as Button
		b_cancel = findViewById(R.id.b_cancel) as Button
		
	}
	override fun onStart() {
		super.onStart()
		
		tv_title.text =
			if (isCreateMode) getString(R.string.Note_create)
			else getString(R.string.Note_edit_id, noteId)
		
		if (isCreateMode) b_delete.visibility=View.GONE
		else b_delete.setOnClickListener {
			NoteDeleteDialog(notebookKey, noteId).show(fragmentManager,null)
		}
		
		
		b_ok.setOnClickListener {
			val newNoteContent = noteContentViewFragment!!.syncTexts(true)
			
			if (isCreateMode) mutableNotebook.addNote(newNoteContent)
			else mutableNotebook.modifyNoteContent(noteId, newNoteContent)
			
			mainActivity.jumpBackFragment()
		}
		b_cancel.setOnClickListener {
			mainActivity.jumpBackFragment()
		}
		
		//update noteContent
		if (isCreateMode) updateNoteContent(QuestionContent("", ""))
		else updateNoteContent(note.content)
		
	}
	
	
	//noteContent
	private var noteContentViewFragment: NoteContentEditFragment? = null
	override fun onAttachFragment(childFragment: Fragment) {
		super.onAttachFragment(childFragment)
		noteContentViewFragment = childFragment as? NoteContentEditFragment
	}
	private fun updateNoteContent(noteContent: NoteContent){
		val updateSucceed = noteContentViewFragment?.setNoteContent(noteContent) == true
		if (!updateSucceed) {
			val fragment = typedNoteContentEditFragments[noteContent.typeTag]?.newInstance()
				?:throw RuntimeException("The noteContent type \"${noteContent.typeTag}\" is not supported.")
			childFragmentManager.beginTransaction()
				.replace(R.id.fl_noteContent_container, fragment)
				.commit()
			fragment.setNoteContent(noteContent)
			noteContentViewFragment =fragment
		}
	}
	
	
}

