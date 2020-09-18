package crawler

import com.typesafe.config.{Config, ConfigFactory}
import crawler.config.ExtendedConfig
import crawler.fetcher.SitesFetcher
import crawler.handlers.FileSavingHandler
import java.util.concurrent.{ExecutorService, Executors}

import scala.io.{BufferedSource, Source}

object Main {

  private def getFileWithSites(config: Config): BufferedSource = {
    val sitesFilename = config.getConfigOption("sites")
                              .flatMap(_.getStringOption("file"))
                              .getOrElse(throw new RuntimeException("Missing sites file in config!"))

    Source.fromFile(sitesFilename)
  }

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load().resolve()

    val threadsNumber = config.getConfigOption("threads")
                              .flatMap(_.getIntOption("max"))
                              .getOrElse(throw new RuntimeException("Missing threads number in config!"))

    val sitesFile = getFileWithSites(config)
    val pool: ExecutorService = Executors.newFixedThreadPool(threadsNumber)

    val fetcher = new SitesFetcher(config.getConfig("fetcher"))
    val fileWriter = new FileSavingHandler()

    try {
      sitesFile.getLines().foreach { site =>
        // TODO added writing to the file in batches, not one by one
        pool.execute(() => fetcher.fetchSite(site).foreach(pageData => fileWriter.writeData(pageData)))
      }
    } finally {
      sitesFile.close()
      pool.shutdown()
    }
  }
}
