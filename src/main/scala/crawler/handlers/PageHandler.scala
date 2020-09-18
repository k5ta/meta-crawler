package crawler.handlers

import crawler.SiteMetadata
import crawler.fetcher.UnknownStatusCodeException
import crawler.parser.HtmlHeadParser
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.ContentType
import org.apache.http.util.EntityUtils
import org.apache.logging.log4j.scala.Logging

object PageHandler extends Logging {

  def processPage(site: String, response: CloseableHttpResponse): Option[SiteMetadata]  = {
    response.getStatusLine.getStatusCode match {
      case 200 =>

      case known: Int if Set(403, 404, 503).contains(known) =>
        logger.error(s"Got $known status code for $site")
        None // FIXME probably collect info also for some of this status codes?

      case other => throw new UnknownStatusCodeException(other, site)
    }

    val html = getDecodedHtml(response)
    val pageData = HtmlHeadParser.parsePageMetadata(html)

    logger.info(s"Successfully processed page metadata for $site")
    Some(SiteMetadata(site, pageData))
  }

  private def getDecodedHtml(response: CloseableHttpResponse): String = {
    val charset = ContentType.getOrDefault(response.getEntity).getCharset
    EntityUtils.toString(response.getEntity, charset)
  }

}
