package crawler.fetcher

class UnknownStatusCodeException(statusCode: Int, url: String)
  extends RuntimeException(s"Got unknown status code $statusCode for url $url")
