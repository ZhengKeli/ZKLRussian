package com.zkl.memruss.control.note

import com.zkl.memruss.control.tools.silence
import com.zkl.memruss.core.note.NoteContent
import com.zkl.memruss.core.note.QuestionContent
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

object NoteContentTextParser {
	
	val fileExtension = "txt"
	
	fun parse(file: File): List<NoteContent> {
		silence { return Scanner(file, "UTF8").parseNoteContents() }
		silence { return Scanner(file, "GBK").parseNoteContents() }
		return emptyList()
	}
	
	fun parse(string: String): List<NoteContent> {
		silence { return Scanner(string).parseNoteContents() }
		return emptyList()
	}
	
	private fun Scanner.parseNoteContents(): List<NoteContent> {
		val contents = ArrayList<NoteContent>()
		while (true) {
			val question = nextNotBlankLineOrNull()?.trim() ?: break
			val answerLines = ArrayList<String>()
			while (true) {
				val answerLine = nextLineOrNull() ?: break
				if (answerLine.isBlank()) break
				answerLines.add(answerLine.trim())
			}
			val answer = answerLines.joinToString("\n")
			if (answer.isNotBlank()) contents.add(QuestionContent(question, answer))
		}
		return contents
	}
	
	private fun Scanner.nextLineOrNull(): String? {
		return if (hasNextLine()) nextLine() else null
	}
	
	private fun Scanner.nextNotBlankLineOrNull(): String? {
		while (true) {
			val line = nextLineOrNull() ?: return null
			if (line.isNotBlank()) return line
		}
	}
	
}