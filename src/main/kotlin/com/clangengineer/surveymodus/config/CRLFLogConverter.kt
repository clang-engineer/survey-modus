package com.clangengineer.surveymodus.config

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.CompositeConverter
import org.slf4j.Marker
import org.slf4j.MarkerFactory
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiElement
import org.springframework.boot.ansi.AnsiOutput
import org.springframework.boot.ansi.AnsiStyle

class CRLFLogConverter : CompositeConverter<ILoggingEvent>() {

    companion object {
        val CRLF_SAFE_MARKER: Marker = MarkerFactory.getMarker("CRLF_SAFE")
        private val SAFE_LOGGERS = listOf("org.hibernate")
        private val ELEMENTS: HashMap<String, AnsiElement> = hashMapOf(
            "faint" to AnsiStyle.FAINT,
            "red" to AnsiColor.RED,
            "green" to AnsiColor.GREEN,
            "yellow" to AnsiColor.YELLOW,
            "blue" to AnsiColor.BLUE,
            "magenta" to AnsiColor.MAGENTA,
            "cyan" to AnsiColor.CYAN
        )
    }

    override fun transform(event: ILoggingEvent, inStr: String): String {
        val element = ELEMENTS[firstOption]
        if ((event.marker != null && event.marker.contains(CRLF_SAFE_MARKER)) || isLoggerSafe(event)) {
            return inStr
        }
        val replacement = if (element == null) "_" else toAnsiString("_", element)
        return inStr.replace("[\n\r\t]", replacement)
    }

    protected fun isLoggerSafe(event: ILoggingEvent): Boolean {
        SAFE_LOGGERS.forEach {
            if (event.loggerName.startsWith(it)) return true
        }
        return false
    }

    protected fun toAnsiString(inStr: String, element: AnsiElement): String {
        return AnsiOutput.toString(element, inStr)
    }
}
