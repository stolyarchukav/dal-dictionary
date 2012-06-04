package forzaverita.brefdic.rest.impl

import forzaverita.brefdic.rest.WordService
import forzaverita.brefdic.model.Word
import org.springframework.stereotype.Service
import org.springframework.context.annotation.Scope

@Service
@Scope("request")
class WordServiceImpl extends WordService {

  def getWord(id : Integer) = {
    val word = new Word()
    word.setId(123)
    word
  }
  
  def test() = "Test method"
  
}