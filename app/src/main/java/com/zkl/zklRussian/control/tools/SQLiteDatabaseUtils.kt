package com.zkl.zklRussian.control.tools

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.SelectQueryBuilder
import org.jetbrains.anko.db.SqlOrderDirection

fun SQLiteDatabase.createIndex(indexName: String, tableName: String,
                               unique: Boolean = false, ifNotExists: Boolean = false, vararg columns: String) {
	val escapedIndexName = indexName.replace("`", "``")
	val escapedTableName = tableName.replace("`", "``")
	val ifNotExistsText = if (ifNotExists) "IF NOT EXISTS" else ""
	val uniqueText = if (unique) "UNIQUE" else ""
	execSQL(
		columns.joinToString(
			separator = ",",
			prefix = "CREATE $uniqueText INDEX $ifNotExistsText `$escapedIndexName` ON `$escapedTableName`(",
			postfix = ");"
		)
	)
}

fun SQLiteDatabase.dropIndex(indexName: String, ifExists: Boolean = false) {
	val escapedIndexName = indexName.replace("`", "``")
	val ifExistsText = if (ifExists) "IF EXISTS" else ""
	execSQL("DROP INDEX $ifExistsText `$escapedIndexName`;")
}

fun SelectQueryBuilder.orderBy(value: String, asc: Boolean = true)
	= orderBy(value, if (asc) SqlOrderDirection.ASC else SqlOrderDirection.DESC)

inline fun <T : Cursor?, R> T.use(block: (T) -> R): R {
	var closed = false
	try {
		return block(this)
	} catch (e: Exception) {
		closed = true
		try {
			this?.close()
		} catch (closeException: Exception) {
		}
		throw e
	} finally {
		if (!closed) {
			this?.close()
		}
	}
}
