package forzaverita.brefdic.rest.impl

import scala.collection.JavaConversions._
import forzaverita.brefdic.rest.common.CommonFunctions._
import forzaverita.brefdic.rest.SearchService
import java.util.ArrayList
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.entity.WordEntity
import org.springframework.stereotype.Service
import org.springframework.context.annotation.Scope
import org.springframework.beans.factory.annotation.Autowired
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.converter.WordConverter
import org.springframework.data.domain.PageRequest

@Service
@Scope("request")
class SearchServiceImpl extends SearchService {
  
  val MAX_RESULTS = 20
  val page = new PageRequest(0, MAX_RESULTS)

  @Autowired
  val wordRepo : WordRepository = null
  
  def getWordsBeginWith(begin : String) = {
    findWords(wordRepo.findByWordLike(begin.toUpperCase() + '%', page), WordConverter toWord _)
  }
  
  def getWordsFullSearch(query : String) = {
    findWords(wordRepo.findByDescriptionLike('%' + query.toLowerCase() + '%', page), WordConverter toWord _)
  }
  
}