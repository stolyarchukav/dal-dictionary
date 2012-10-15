package forzaverita.brefdic.entity

import javax.persistence._
import forzaverita.brefdic.model.Word

@Entity
class WordEntity (pId : Integer, pWord : String, pDescription : String, pFirstLetter : String) {

  def this (word : Word) = this (word.getId, word.getWord, word.getDescription, word.getFirstLetter) 
 
  def this () = this (null, null, null, null)
  
  @Id 
  var id : Integer = pId
  
  var word : String = pWord
  
  @Column(length = 70000)
  var description : String = pDescription
  
  var firstLetter : String = pFirstLetter
  
}