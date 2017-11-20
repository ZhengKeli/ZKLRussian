package com.zkl.zklRussian.ui

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zkl.zklRussian.R
import com.zkl.zklRussian.control.note.NotebookKey
import com.zkl.zklRussian.core.note.MutableNotebook
import com.zkl.zklRussian.core.note.Note

class NotebookFragment : NotebookHoldingFragment() {
	
	companion object {
		fun newInstance(notebookKey: NotebookKey)
			= NotebookFragment::class.java.newInstance(notebookKey)
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
		= inflater.inflate(R.layout.fragment_notebook, container, false).apply {
		
		val b_back = findViewById(R.id.b_back) as ImageButton
		val tv_title = findViewById(R.id.tv_title) as TextView
		val sv_search = findViewById(R.id.sv_search) as SearchView
		val tv_bookInfo = findViewById(R.id.tv_bookInfo) as TextView
		val b_memoryPlan = findViewById(R.id.b_memoryPlan) as ImageButton
		val b_addNote = findViewById(R.id.b_addNote) as ImageButton
		val cl_review = findViewById(R.id.cl_review) as ConstraintLayout
		val tv_review = findViewById(R.id.tv_review) as TextView
		val b_review = findViewById(R.id.b_review) as Button
		val lv_notes = findViewById(R.id.lv_notes) as ListView
		
		//top bar
		b_back.setOnClickListener {
			fragmentManager.popBackStack()
		}
		tv_title.text = notebook.name
		sv_search.setOnSearchClickListener {
			fragmentManager.jumpTo(NotebookSearchFragment.newInstance(notebookKey),true)
			sv_search.isIconified = true
		}
		
		//info bar
		tv_bookInfo.text = getString(R.string.count_Notes_in_all, notebook.noteCount)
		if (notebook is MutableNotebook) {
			b_addNote.visibility = View.VISIBLE
			b_addNote.setOnClickListener {
				val fragment = NoteEditFragment.newInstance(notebookKey, -1)
				fragmentManager.jumpTo(fragment,true)
			}
			b_memoryPlan.visibility = View.VISIBLE
			b_memoryPlan.setOnClickListener{
				fragmentManager.jumpTo(MemoryPlanFragment.newInstance(notebookKey),true)
			}
		}
		else {
			b_addNote.visibility = View.GONE
			b_memoryPlan.visibility = View.GONE
		}
		
		//review bar
		fun updateNeedReview(){
			if (notebook is MutableNotebook) {
				mutableNotebook.fillNotesByPlan()
				val needReviewCount = notebook.countNeedReviewNotes(System.currentTimeMillis())
				if (needReviewCount > 0) {
					cl_review.visibility = View.VISIBLE
					tv_review.text = getString(R.string.need_review,needReviewCount)
				}
			}
		}
		if(notebook is MutableNotebook){
			b_review.setOnClickListener {
				fragmentManager.jumpTo(NoteReviewFragment.newInstance(notebookKey),true)
			}
			updateNeedReview()
		}
		
		//list
		notesBuffer.clearBuffer()
		lv_notes.adapter = object :NoteListAdapter(){
			override fun getCount() = notesBuffer.size
			override fun getItem(position: Int) = notesBuffer[position]
			override val context: Context get() = activity
		}
		lv_notes.setOnItemClickListener { _, _, position, _ ->
			val note = notesBuffer[position]
			val fragment = NoteViewFragment.newInstance(notebookKey, note.id)
			fragmentManager.jumpTo(fragment,true)
		}
	}
	
	//notes buffer
	private val notesBuffer = object : SectionBufferList<Note>(){
		override fun getSection(startFrom: Int): List<Note>
			= notebook.selectLatestNotes(sectionSize, startFrom)
		override val size: Int
			get() = notebook.noteCount
	}
	
}

