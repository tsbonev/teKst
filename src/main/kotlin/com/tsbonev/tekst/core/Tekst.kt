package com.tsbonev.tekst.core

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
interface Tekst {
	fun makeChange(id: String, text: String): HistoryChange

	fun viewHistory(id: String): List<HistoryChange>
}

data class HistoryChange(
	val id: String, val version: Int = 0,
	val insertions: List<Insertion>
)

data class Insertion(var version: Int = 0, val start: Int, val end: Int, val content: String)