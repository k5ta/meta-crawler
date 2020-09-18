package object crawler {

  case class PageData(
    title: String,
    keywords: String,
    descriptions: String
  ) {
    def toCSVFormat: String = {
      s"${title.wrapIntoQuote},${keywords.wrapIntoQuote},${descriptions.wrapIntoQuote}\n"
    }
  }

  case class SiteMetadata(
    site: String,
    pageData: PageData
  ) {
    def toCSVFormat: String = s"$site,${pageData.toCSVFormat}"
  }


  implicit class StringQuotingExtension(data: String) {
    def wrapIntoQuote: String = {
      if (data.isEmpty) {
        data
      } else {
        s"""\"$data\""""
      }
    }
  }

}
