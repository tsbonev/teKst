package com.tsbonev.tekst.core

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class InMemoryTekstRecorder(private val tekstBuilder: TekstBuilder) : TekstRecorder {
	private	val historyMap = mutableMapOf<String, MutableList<ChangeLog>>()

	override fun view(id: String): List<ChangeLog> {
		return historyMap[id] ?: listOf()
	}

	override fun edit(id: String, text: String): ChangeLog {
		val history = historyMap[id]
		return when (history) {
			null -> {
				val change = ChangeLog(id, 0, listOf(Change.Insertion(0, 0, text)))
				historyMap[id] = mutableListOf(change)
				change
			}

			else -> {
				val oldContent = tekstBuilder.build(history)

				return if (text == oldContent) history.last()
				else {
					val listOfChanges = oldContent.getReplacementDifferences(text)

					val change = ChangeLog(id, history.size, listOfChanges)
					history.add(change)

					change
				}
			}
		}
	}

	/**
	 * Returns the replacement differences between two strings.
	 */
	private fun String.getReplacementDifferences(text: String): List<Change.Replacement> {
		val lenDifference = text.length - this.length

		return when (lenDifference) {

			0 -> getReplacementChanges(this, text)

			else -> listOf()
		}
	}

	/**
	 * Returns a list of changes that constitute a replacement.
	 */
	private fun getReplacementChanges(oldText: String, newText: String): List<Change.Replacement> {
		val insertions = mutableListOf<Change.Replacement>()

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

					insertions.add(
						Change.Replacement(
							insertions.size,
							changeStart,
							changeEnd,
							changeContent.toString()
						)
					)
					insertionBuildStarted = false
					changeContent.clear()
				}
			}

		return insertions
	}
}