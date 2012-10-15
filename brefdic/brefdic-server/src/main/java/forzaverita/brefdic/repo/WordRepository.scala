package forzaverita.brefdic.repo

import org.springframework.data.jpa.repository.JpaRepository
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.entity.WordEntity
import java.util.List
import org.springframework.data.domain.Pageable

trait WordRepository extends JpaRepository[WordEntity, Integer] {
  
  def findByWordLike(query : String, pageable : Pageable) : List[WordEntity]
  
  def findByDescriptionLike(query : String, pageable : Pageable) : List[WordEntity]
  
}