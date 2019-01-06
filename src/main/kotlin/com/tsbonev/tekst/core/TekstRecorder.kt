package com.tsbonev.tekst.core

/**
 * Recorders are what compare two texts and record the [ChangeLog]
 * that separates them.
 *
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
interface TekstRecorder {

	/**
	 * Edits a text and saves the changes.
	 *
	 * @param id The id of the text to edit.
	 * @param text The new text to get changes from.
	 * @return A list of applied changes.
	 */
	fun edit(id: String, text: String): ChangeLog

	/**
	 * Views the history of a given text.
	 *
	 * @param id The id of the text.
	 * @return A list of all changes.
	 */
	fun view(id: String): List<ChangeLog>
}