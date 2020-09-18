package crawler.fetcher

import java.io.IOException
import java.net.{IDN, SocketException, SocketTimeoutException, UnknownHostException}

import crawler.config._
import com.typesafe.config.Config
import crawler.SiteMetadata
import crawler.handlers.PageHandler
import org.apache.http.NoHttpResponseException
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.{ConnectionPoolTimeoutException, HttpHostConnectException}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.client.config.RequestConfig
import org.apache.logging.log4j.scala.Logging

class SitesFetcher(config: Config) extends Logging {

  import SitesFetcher._

  private val httpScheme: HttpScheme.Value = config.getEnumerationValueOption("scheme", HttpScheme).getOrElse(DEFAULT_HTTP_SCHEME)
  private val endBytes: Option[Int] = config.getIntOption("bytes-to")

  def fetchSite(site: String): Option[SiteMetadata] = {
    logger.info(s"Fetching site $site")

    val asciiFormattedSite = IDN.toASCII(site)
    val request = createGetRequest(asciiFormattedSite, httpScheme)

    try {
      val response = client.execute(request)
      PageHandler.processPage(site, response)
    } catch {
      case _: IOException => None // some socket and etc exceptions
    }
  }

  private def createGetRequest(site: String, scheme: HttpScheme.Value): HttpGet = {
    val request = new HttpGet(s"${scheme.toString}://$site")

    endBytes.foreach(bytes => request.addRangeHeader(endBytes = bytes))
    request.setConnectionTimeout()

    request
  }
}

object SitesFetcher {

  private lazy val client = HttpClientBuilder.create().build()

  private val DEFAULT_HTTP_SCHEME: HttpScheme.Value = HttpScheme.Http

  private val DEFAULT_CONNECTION_TIMEOUT = 30 * 1000 // 30 seconds


  implicit class RequestSettings(getRequest: HttpGet) {
    def addRangeHeader(startBytes: Int = 0, endBytes: Int): Unit = {
      getRequest.addHeader("Range", s"bytes=$startBytes-$endBytes")
    }

    def setConnectionTimeout(timeout: Int = DEFAULT_CONNECTION_TIMEOUT): Unit = {
      val requestConfig = RequestConfig.custom
      requestConfig.setConnectTimeout(timeout)
      requestConfig.setConnectionRequestTimeout(timeout)
      requestConfig.setSocketTimeout(timeout)

      getRequest.setConfig(requestConfig.build)
    }
  }

}
