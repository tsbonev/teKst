package com.tsbonev.tekst.core

import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is
import org.junit.Assert.assertThat

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class InMemoryTekstTest {
	private lateinit var tekst : Tekst

	@Before
	fun setUp(){
		tekst = InMemoryTekst()
	}

	@Test
	fun `First change creates text`() {
		val change = tekst.makeChange("::id::", "::content::")

		val history = tekst.viewHistory("::id::")

		assertThat(history, containsInAnyOrder(change))
	}

	@Test
	fun `Equivalent changes return the last change and doesn't change history`() {
		val firstChange = tekst.makeChange("::id::", "::content::")
		val secondChange = tekst.makeChange("::id::", "::content::")

		val history = tekst.viewHistory("::id::")

		assertThat(firstChange, Is(secondChange))
		assertThat(history, containsInAnyOrder(firstChange))
	}

	@Test
	fun `Second change is replacement and gets recorded`() {
		val firstChange = tekst.makeChange("::id::", "::content::")
		val secondChange = tekst.makeChange("::id::", "[:ConTENT:]")

		val history = tekst.viewHistory("::id::")

		assertThat(history, containsInAnyOrder(firstChange, secondChange))
	}

	@Test
	fun `Multiple replacement changes get recorded`() {
		val firstChange = tekst.makeChange("::id::", "::content::")
		val secondChange = tekst.makeChange("::id::", "[:content:]")
		val thirdChange = tekst.makeChange("::id::", ":[conTent]:")
		val fourthChange = tekst.makeChange("::id::", ":[ConTenT]:")

		val history = tekst.viewHistory("::id::")

		assertThat(history, containsInAnyOrder(firstChange, secondChange, thirdChange, fourthChange))
	}
}