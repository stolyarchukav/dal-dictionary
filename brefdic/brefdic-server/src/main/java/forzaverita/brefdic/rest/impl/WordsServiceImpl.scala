package forzaverita.brefdic.rest.impl

import java.util.ArrayList
import java.util.Collection
import scala.collection.JavaConversions._
import forzaverita.brefdic.rest.common.CommonFunctions._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import forzaverita.brefdic.converter.WordConverter
import forzaverita.brefdic.entity.WordEntity
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.rest.WordsService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction

@Service
@Scope("request")
class WordsServiceImpl extends WordsService {

  @Autowired
  val wordRepo : WordRepository = null
  
  def getWords(user : String) = {
    findWords(wordRepo findAll, WordConverter toWord _)
  }
  
  def getIndexWords = {
    findWords(wordRepo findAll, WordConverter toIndexWord _)
  }
  
  def getIndexWords(page : Int, size : Int) = {
    findWords(wordRepo.findAll(new PageRequest(page, size, Direction.ASC, "id")), WordConverter toIndexWord _)
  }
  
  def getWordsCount(user : String) = {
    wordRepo count
  }
  
}