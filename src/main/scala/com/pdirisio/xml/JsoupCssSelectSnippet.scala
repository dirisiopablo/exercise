package com.pdirisio.xml

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.util.Try

object JsoupCssSelectSnippet extends LazyLogging {

  val CHARSET_NAME = "utf8"

  def findElementsByQuery(htmlFile: File, cssQuery: String): Try[Elements] = Try {
    Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath)
  }.map(_.select(cssQuery))
}
