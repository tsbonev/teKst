package com.tsbonev.tekst.core

import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class SimpleTekstBuilderTest {

	private val tekstBuilder = SimpleTekstBuilder()

	@Test
	fun `Builds text from short history`() {
		@Suppress("UNUSED_VARIABLE")
		val initial = "The red fox has big ears."
		val result = "The furry red fox has big, long"

		val changelist = listOf(
			ChangeLog(
				"::id::", 0, listOf(
					Change.Replacement(0, 0, 24, "The red fox has big ears.")
				)
			),
			ChangeLog(
				"::id::", 1, listOf(
					Change.Insertion(0, 4, "furry "),
					Change.Replacement(1, 22, 24, "big,"),
					Change.Insertion(2, 27, "long "),
					Change.Deletion(3, 31, 37)
				)
			)
		)

		val builtResult = tekstBuilder.build(changelist)

		assertThat(builtResult, Is(result))
	}


	@Test
	@Suppress("UNUSED_VARIABLE")
	fun `Builds text from longer history`() {
		val initial = "The red fox has big ears."
		val changeOneInsertion = "The furry red fox has big ears."
		val changeTwoReplacementsAndAnInsertion = "The furriest red fox had the biggest, longest ears."
		val changeThreeDeletion = "The furriest red fox had the biggest ears."

		val changelist = listOf(
			ChangeLog(
				"::id::", 0, listOf(
					Change.Replacement(0, 0, 24, "The red fox has big ears.")
				)
			),
			ChangeLog(
				"::id::", 1, listOf(
					Change.Insertion(0, 4, "furry ")
				)
			),
			ChangeLog(
				"::id::", 2, listOf(
					Change.Replacement(0, 4, 8, "furriest"),
					Change.Replacement(1, 21, 27, "had the biggest,"),
					Change.Insertion(2, 38, "longest ")
				)
			),
			ChangeLog(
				"::id::", 3, listOf(
					Change.Deletion(0, 36, 45)
				)
			)
		)

		val firstResult = tekstBuilder.build(changelist.subList(0, 2))
		val secondResult = tekstBuilder.build(changelist.subList(0, 3))
		val thirdResult = tekstBuilder.build(changelist.subList(0, 4))

		assertThat(firstResult, Is(changeOneInsertion))
		assertThat(secondResult, Is(changeTwoReplacementsAndAnInsertion))
		assertThat(thirdResult, Is(changeThreeDeletion))
	}
}