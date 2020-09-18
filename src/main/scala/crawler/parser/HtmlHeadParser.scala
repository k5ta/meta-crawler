package crawler.parser

import crawler.PageData
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._

object HtmlHeadParser {

  def parsePageMetadata(html: String): PageData = {
    val document = Jsoup.parse(html)
    val head = document.head()

    PageData(
      title = getPageTitle(head).getOrElse(""), // FIXME probably change this 'getOrElse' logic
      keywords = getMetaKeywords(head),
      descriptions = getMetaDescriptions(head)
    )
  }


  private def getPageTitle(head: Element): Option[String] = {
    head.getElementsByTag("title").asScala.headOption.map(_.text())
  }

  private def getMetaKeywords(head: Element): String = {
    getElementContentAttribute(head, query = "meta[name=keywords]", separator = ",")
  }

  private def getMetaDescriptions(head: Element): String = {
    getElementContentAttribute(head, query = "meta[name=description]", separator = " ")
  }

  private def getElementContentAttribute(head: Element, query: String, separator: String): String = {
    head.select(query).asScala.map(_.attr("content")).mkString(separator)
  }

}
