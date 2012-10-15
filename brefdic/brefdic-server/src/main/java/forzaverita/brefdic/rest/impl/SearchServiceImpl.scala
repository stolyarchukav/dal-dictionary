package forzaverita.brefdic.rest.impl

import scala.collection.JavaConversions.asScalaBuffer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

import forzaverita.brefdic.converter.WordConverter
import forzaverita.brefdic.datasource.DataSourceHolder
import forzaverita.brefdic.model.wrapper.WordWrapper
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.rest.common.CommonFunctions.findWords
import forzaverita.brefdic.rest.SearchService

@Service
@Scope("request")
class SearchServiceImpl extends SearchService {

  val MAX_RESULTS = 20
  val page = new PageRequest(0, MAX_RESULTS)

  @Autowired
  val wordRepo: WordRepository = null

  def getWordsBeginWith(begin: String) = {
    DataSourceHolder := 1
    val words = new WordWrapper().append(findBegin(begin))
    DataSourceHolder := 2
    words.append(findBegin(begin))
  }

  def getWordsFullSearch(query: String) = {
    DataSourceHolder := 1
    val words = new WordWrapper().append(fullSearch(query))
    DataSourceHolder := 2
    words.append(fullSearch(query))
  }

  private def findBegin(begin: String) = {
    findWords(wordRepo.findByWordLike(begin.toUpperCase() + '%', page), WordConverter toWord _)
  }

  private def fullSearch(query: String) = {
    findWords(wordRepo.findByDescriptionLike('%' + query.toLowerCase() + '%', page), WordConverter toWord _)
  }

}