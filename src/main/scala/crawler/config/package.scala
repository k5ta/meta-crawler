package crawler

import com.typesafe.config.Config
import com.typesafe.config.ConfigException

package object config {

  implicit class ExtendedConfig(config: Config) {

    def getEnumerationValue[T <: Enumeration](path: String, enumeration: T): T#Value = {
      val name = config.getString(path)
      val enumValues = enumeration.values

      enumValues.find(_.toString.toLowerCase == name.toLowerCase)
                .getOrElse {
                  throw new ConfigException.BadValue(path, noSuchEnumValueMessage(name, enumeration.getClass, enumValues.map(_.toString).toSeq))
                }
    }

    private def noSuchEnumValueMessage(valueName: String, enumClass: Class[_], existingValueNames: Seq[String]) =
      s"The enumeration class ${enumClass.getSimpleName} has no constant of the name '$valueName' (should be one of ${existingValueNames.mkString("[", ", ", "]")})"


    def getIntOption(path: String): Option[Int] = getOption(path, config.getInt)

    def getStringOption(path: String): Option[String] = getOption(path, config.getString)

    def getConfigOption(path: String): Option[Config] = getOption(path, config.getConfig)

    def getEnumerationValueOption[T <: scala.Enumeration](path: String, enumeration: T): Option[T#Value] =
      getOption(path, getEnumerationValue(_, enumeration))


    private def getOption[T](path: String, getter: String => T): Option[T] = {
      if (config.hasPath(path)) Some(getter(path)) else None
    }
  }

}