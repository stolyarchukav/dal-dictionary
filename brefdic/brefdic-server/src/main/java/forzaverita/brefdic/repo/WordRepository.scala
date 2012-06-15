package forzaverita.brefdic.repo

import org.springframework.data.jpa.repository.JpaRepository
import forzaverita.brefdic.model.Word
import forzaverita.brefdic.entity.WordEntity

trait WordRepository extends JpaRepository[WordEntity, Integer] {
  
}