package com.tsbonev.tekst.core

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class SimpleTekstBuilder : TekstBuilder {
	override fun build(changelog: List<ChangeLog>): String {
		val text = StringBuilder()

		changelog.sortedBy { it.version }
			.forEach { log ->

				log.changes.sortedBy { it.version }
					.forEach { change ->
						when(change){
							is Change.Insertion -> {
								text.insert(change.start, change.content)
							}

							is Change.Replacement -> {
								text.replace(change.start, change.end + 1, change.content)
							}

							is Change.Deletion -> {
								text.delete(change.start, change.end)
							}
						}
					}
			}
		return text.toString()
	}
}