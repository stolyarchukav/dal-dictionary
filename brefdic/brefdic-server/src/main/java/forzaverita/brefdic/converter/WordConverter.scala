package forzaverita.brefdic.converter

import forzaverita.brefdic.entity.WordEntity
import forzaverita.brefdic.model.Word
import javax.persistence.Entity

object WordConverter {

  def toWord(entity: WordEntity) = {
    val word = new Word
    if (entity != null) {
      word setId entity.id
      word setWord entity.word
      word setDescription entity.description
      word setFirstLetter entity.firstLetter
    }
    word
  }

  def toIndexWord(entity: WordEntity) = {
    val word = new Word
    if (entity != null) {
      word setId entity.id
      word setWord entity.word
      word setDescription entity.description
    }
    word
  }

}