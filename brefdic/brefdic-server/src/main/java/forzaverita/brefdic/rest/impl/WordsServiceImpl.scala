package forzaverita.brefdic.rest.impl

import java.util.ArrayList
import java.util.Collection

import scala.collection.JavaConversions._

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

import forzaverita.brefdic.converter.WordConverter
import forzaverita.brefdic.entity.WordEntity
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.rest.WordsService

@Service
@Scope("request")
class WordsServiceImpl extends WordsService {

  @Autowired
  val wordRepo : WordRepository = null
  
  def getWords : Collection[Word] = {
    val result = new ArrayList[Word]()
    val entities = (wordRepo findAll)
    entities foreach(result += WordConverter toWord _)
    result
  }
  
  def getIndexWords : Collection[Word] = {
    val result = new ArrayList[Word]()
    val entities = (wordRepo findAll)
    entities foreach(result += WordConverter toIndexWord _)
    result
  }
  
}