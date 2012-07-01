package forzaverita.brefdic.rest.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import forzaverita.brefdic.entity.WordEntity
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.rest.WordService
import scala.collection.JavaConversions._
import scala.collection.mutable.MutableList
import forzaverita.brefdic.converter.WordConverter

@Service
@Scope("request")
class WordServiceImpl extends WordService {

  @Autowired
  val wordRepo : WordRepository = null
  
  def getWord(id : Integer) = {
    val entity = wordRepo findOne id
    WordConverter toWord entity
  }
  
  def postWord(word : Word) = {
    val entity = new WordEntity(word)
    WordConverter toWord (wordRepo saveAndFlush entity)
  }
  
  def putWord(id : Integer, word : Word) = {
    if (id != word.getId()) throw new Exception()
    val entity = new WordEntity(word)
    WordConverter toWord (wordRepo saveAndFlush entity)
  }
  
  def deleteWord(id : Integer) = {
    wordRepo delete id
  }
  
}