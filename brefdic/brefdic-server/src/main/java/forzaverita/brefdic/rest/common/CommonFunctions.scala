package forzaverita.brefdic.rest.common

import java.util.ArrayList
import scala.collection.JavaConversions._
import forzaverita.brefdic.entity.WordEntity
import forzaverita.brefdic.model.Word

object CommonFunctions {

  def findWords(finder : => Iterable[WordEntity], converter : WordEntity => Word) = {
    val result = new ArrayList[Word]()
    val entities = finder
    entities foreach(result += converter(_))
    result
  }
  
}