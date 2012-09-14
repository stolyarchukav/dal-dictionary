package forzaverita.brefdic.rest.impl

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.iterableAsScalaIterable

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

import forzaverita.brefdic.converter.WordConverter
import forzaverita.brefdic.datasource.DataSourceHolder
import forzaverita.brefdic.datasource.DataSourceResolver
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.rest.common.CommonFunctions.findWords
import forzaverita.brefdic.rest.WordsService
import forzaverita.brefdic.datasource.DataSourceResolver._

@Service
@Scope("request")
class WordsServiceImpl extends WordsService {

  @Autowired
  val wordRepo: WordRepository = null

  def getWords(user: String) = {
    findWords(wordRepo findAll, WordConverter toWord _)
  }

  def getIndexWords = {
    findWords(wordRepo findAll, WordConverter toIndexWord _)
  }

  def getIndexWords(page: Int, size: Int) = {
    DataSourceHolder := resolve(calculateWordId(page, size))
    findWords(wordRepo.findAll(new PageRequest(resolvePage(page, size), size, Direction.ASC, "id")),
        WordConverter toIndexWord _)
  }

  def getWordsCount(user: String) = {
    DataSourceHolder := 2
    bound + wordRepo.count
  }

}