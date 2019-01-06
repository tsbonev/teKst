package com.tsbonev.tekst.core

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class InMemoryTekst: Tekst {
	private	val historyMap = mutableMapOf<String, MutableList<HistoryChange>>()

	override fun viewHistory(id: String): List<HistoryChange> {
		return historyMap[id] ?: listOf()
	}

	override fun makeChange(id: String, text: String): HistoryChange {
		val history = historyMap[id]
		return when (history) {
			null -> {
				val change = HistoryChange(id, 0, listOf(Insertion(0, 0, text.length - 1, text)))
				historyMap[id] = mutableListOf(change)
				change
			}

			else -> {
				val oldContent = history.buildText()

				return if (text == oldContent) history.last()
				else {
					val listOfChanges = oldContent.getInsertionChanges(text)

					val change = HistoryChange(id, history.size, listOfChanges)
					history.add(change)

					change
				}
			}
		}
	}

	/**
	 * Builds a string from history.
	 */
	private fun List<HistoryChange>.buildText(): String {
		val text = StringBuilder()

		this.sortedBy { it.version }
			.forEach { change ->
				change.insertions.sortedBy { it.version }
					.forEach { insertion ->
						text.replace(insertion.start, insertion.end + 1, insertion.content)
					}
			}
		return text.toString()
	}

	/**
	 * Returns the insertion differences between two strings.
	 */
	private fun String.getInsertionChanges(text: String): List<Insertion> {
		val lenDifference = text.length - this.length

		return when (lenDifference) {

			0 -> replaceInsertions(this, text)

			else -> listOf()
		}
	}

	/**
	 * Returns a list of insertions that constitute a replacement.
	 */
	private fun replaceInsertions(oldText: String, newText: String): List<Insertion> {
		val insertions = mutableListOf<Insertion>()

		val oldSequence = oldText.asSequence()
		val newSequence = newText.asSequence()

		var inChange : Boolean
		var insertionBuildStarted = false

		var changeStart = 0
		var changeEnd: Int
		val changeContent = StringBuilder()

		val endIndex = oldText.length - 1

		oldSequence.zip(newSequence)
			.forEachIndexed { index, pair ->
				inChange = pair.first != pair.second

				if(inChange) {
					if(!insertionBuildStarted) {
						insertionBuildStarted = true
						changeStart = index
					}

					changeContent.append(pair.second)

					if(index == endIndex) inChange = false
				}

				if(insertionBuildStarted && !inChange){
					changeEnd = if(index == endIndex) index else index - 1

					insertions.add(Insertion(insertions.size, changeStart, changeEnd, changeContent.toString()))
					insertionBuildStarted = false
					changeContent.clear()
				}
			}

		return insertions
	}
}