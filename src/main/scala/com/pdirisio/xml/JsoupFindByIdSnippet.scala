package com.pdirisio.xml

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.util.Try

object JsoupFindByIdSnippet extends LazyLogging {

  private val CHARSET_NAME = "utf8"

  def findElementById(htmlFile: File, targetElementId: String): Try[Element] = Try {
    Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath)
  }.map(_.getElementById(targetElementId))

}
