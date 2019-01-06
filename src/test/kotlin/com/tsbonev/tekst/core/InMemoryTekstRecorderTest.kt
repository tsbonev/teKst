package com.tsbonev.tekst.core

import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is
import org.junit.Assert.assertThat

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class InMemoryTekstRecorderTest {
	private lateinit var tekst : TekstRecorder

	private val simpleTekstBuilder = SimpleTekstBuilder()

	@Before
	fun setUp(){
		tekst = InMemoryTekstRecorder(simpleTekstBuilder)
	}

	@Test
	fun `First change creates text`() {
		val change = tekst.edit("::id::", "::content::")

		val history = tekst.view("::id::")

		assertThat(history, containsInAnyOrder(change))
	}

	@Test
	fun `Equivalent changes return the last change and doesn't change history`() {
		val firstChange = tekst.edit("::id::", "::content::")
		val secondChange = tekst.edit("::id::", "::content::")

		val history = tekst.view("::id::")

		assertThat(firstChange, Is(secondChange))
		assertThat(history, containsInAnyOrder(firstChange))
	}

	@Test
	fun `Second change is replacement and gets recorded`() {
		val firstChange = tekst.edit("::id::", "::content::")
		val secondChange = tekst.edit("::id::", "[:ConTENT:]")

		val history = tekst.view("::id::")

		assertThat(history, containsInAnyOrder(firstChange, secondChange))
	}

	@Test
	fun `Multiple replacement changes get recorded`() {
		val firstChange = tekst.edit("::id::", "::content::")
		val secondChange = tekst.edit("::id::", "[:content:]")
		val thirdChange = tekst.edit("::id::", ":[conTent]:")
		val fourthChange = tekst.edit("::id::", ":[ConTenT]:")

		val history = tekst.view("::id::")

		assertThat(history, containsInAnyOrder(firstChange, secondChange, thirdChange, fourthChange))
	}
}