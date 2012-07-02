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
import org.springframework.data.domain.PageRequest

@Service
@Scope("request")
class WordsServiceImpl extends WordsService {

  @Autowired
  val wordRepo : WordRepository = null
  
  def getWords = {
    getWords(wordRepo findAll, WordConverter toWord _)
  }
  
  def getIndexWords = {
    getWords(wordRepo findAll, WordConverter toIndexWord _)
  }
  
  def getIndexWords(page : Int, size : Int) = {
    getWords(wordRepo.findAll(new PageRequest(page, size)), WordConverter toIndexWord _)
  }
  
  def getWordsCount = {
    wordRepo count
  }
  
  private def getWords(finder : => Iterable[WordEntity], converter : WordEntity => Word) = {
    val result = new ArrayList[Word]()
    val entities = finder
    entities foreach(result += converter(_))
    result
    
  }
  
}