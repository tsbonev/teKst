package com.tsbonev.tekst.core

/**
 * Builders are utilized by recorders to compile a text from a
 * series of [ChangeLog].
 *
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
interface TekstBuilder {

	/**
	 * Builds text from a history of changes.
	 *
	 * @param changelog The list of [ChangeLog].
	 * @return A [String] built from the history.
	 */
	fun build(changelog: List<ChangeLog>): String

}