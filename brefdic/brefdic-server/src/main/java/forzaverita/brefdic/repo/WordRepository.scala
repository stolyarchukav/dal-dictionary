package forzaverita.brefdic.repo

import org.springframework.data.jpa.repository.JpaRepository
import forzaverita.brefdic.model.Word

trait WordRepository extends JpaRepository[Word, Integer] {

  
  
}