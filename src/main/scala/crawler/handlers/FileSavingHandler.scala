package crawler.handlers

import java.io.{BufferedWriter, File, FileWriter}

import crawler.SiteMetadata

class FileSavingHandler(filename: String = "output.csv") {
  private val outputFile = new File(filename)

  // TODO added batch writing to the file
  def writeData(data: SiteMetadata): Unit = {
    synchronized {
      val bufferedWriter = new BufferedWriter(new FileWriter(outputFile, true))
      bufferedWriter.write(data.toCSVFormat)
      bufferedWriter.close()
    }
  }
}
