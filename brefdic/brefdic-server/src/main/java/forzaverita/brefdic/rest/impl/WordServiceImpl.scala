package forzaverita.brefdic.rest.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

import forzaverita.brefdic.converter.WordConverter
import forzaverita.brefdic.datasource.DataSourceHolder
import forzaverita.brefdic.datasource.DataSourceResolver
import forzaverita.brefdic.entity.WordEntity
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.rest.WordService

@Service
@Scope("request")
class WordServiceImpl extends WordService {

  @Autowired
  val wordRepo: WordRepository = null

  def getWord(id: Integer) = {
    prepare(id)
    val entity = wordRepo findOne id
    WordConverter toWord entity
  }

  def postWord(user: String, word: Word) = {
    val entity = new WordEntity(word)
    prepare(word.getId())
    WordConverter toWord (wordRepo saveAndFlush entity)
  }

  def putWord(user: String, id: Integer, word: Word) = {
    if (id != word.getId()) throw new Exception()
    val entity = new WordEntity(word)
    prepare(id)
    WordConverter toWord (wordRepo saveAndFlush entity)
  }

  def deleteWord(user: String, id: Integer) = {
    prepare(id)
    wordRepo delete id
  }

  private def prepare(id: Integer) {
    DataSourceHolder := DataSourceResolver.resolve(id)
  }

}