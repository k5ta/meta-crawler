package crawler

package object fetcher {

  object HttpScheme extends Enumeration {
    type HttpScheme = Value

    val Http: HttpScheme = Value("http")
    val Https: HttpScheme = Value("https")
  }

}
