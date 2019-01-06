package com.tsbonev.tekst.core

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
data class ChangeLog(
	val id: String, val version: Int = 0,
	val changes: List<Change>
)

sealed class Change {
	abstract val version: Int

	data class Replacement(override val version: Int = 0, val start: Int, val end: Int, val content: String) : Change()

	data class Insertion(override val version: Int = 0, val start: Int, val content: String) : Change()

	data class Deletion(override val version: Int = 0, val start: Int, val end: Int) : Change()
}