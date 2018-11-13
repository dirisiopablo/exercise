package com.pdirisio

import java.io.File

import com.pdirisio.xml.JsoupCssSelectSnippet
import com.pdirisio.xml.JsoupFindByIdSnippet
import com.typesafe.scalalogging.LazyLogging
import org.jsoup.nodes.Element

import scala.collection.JavaConversions._
import scala.util.{Failure, Success}

object CrawlerMain extends LazyLogging {
  def main(args: Array[String]): Unit = {
    logger.info("1st Argument {}", args(0))
    logger.info("2nd Argument {}", args(1))
    val sourceFile = new File(args(0))
    val targetElementId = "make-everything-ok-button"

    JsoupFindByIdSnippet.findElementById(sourceFile, targetElementId).map { button =>
      val sourceAttributes = button.attributes().asList().filter(attr => attr.getKey.equals("class") || attr.getKey.equals("title"))
      val cssQuery = button.tag() + sourceAttributes.map( attr => "[" + attr.getKey + "*=" + attr.getValue + "]").mkString(",")
      val targetFile = new File(args(1))

      JsoupCssSelectSnippet.findElementsByQuery(targetFile, cssQuery).map { elements =>
        elements.map(element => (element, elementWeight(element, button))).maxBy( tuple => tuple._2)
      } match {
        case Success(element: (Element, Double)) => logger.info("Target element path: {}", absPath(element._1))
        case Failure(ex) => logger.error("Error occurred.", ex)
      }
    }
  }

  def elementWeight(element: Element, sourceElement: Element): Double = {
    val conditions = Array(
      (elem: Element) => elem.attr("title") != null && elem.attr("title").equals(sourceElement.attr("title")),
      (elem: Element) => elem.attr("class") != null && elem.attr("class").equals(sourceElement.attr("class")),
    )

    conditions.count(fn => fn(element)) / conditions.length
  }

  def absPath(element: Element): String = {
    if (element.id().length == 0) element.cssSelector()
    else absPath(element.parent())
  }
}