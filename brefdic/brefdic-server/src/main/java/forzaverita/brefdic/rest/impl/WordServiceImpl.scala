package forzaverita.brefdic.rest.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

import forzaverita.brefdic.entity.WordEntity
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.repo.WordRepository
import forzaverita.brefdic.rest.WordService
import javax.ws.rs.Consumes
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Service
@Scope("request")
class WordServiceImpl extends WordService {

  @Autowired
  val wordRepo : WordRepository = null
  
  def getWord(id : Integer) = {
    val entity = wordRepo.findOne(id)
    toWord(entity)
  }
  
  def postWord(word : Word) : Word = {
    println(word)
    val entity = new WordEntity(word)
    toWord(wordRepo.saveAndFlush(entity))
  }
  
  def putWord(id : Integer, word : Word) : Word = {
    if (id != word.getId()) throw new Exception()
    val entity = new WordEntity(word)
    toWord(wordRepo.saveAndFlush(entity))
  }
  
  private def toWord(entity : WordEntity) = {
    val word = new Word
    word.setId(entity.id)
    word.setWord(entity.word)
    word.setDescription(entity.description)
    word.setFirstLetter(entity.firstLetter)
    word
  }
  
}