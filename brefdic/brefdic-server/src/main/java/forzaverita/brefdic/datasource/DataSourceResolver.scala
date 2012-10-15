package forzaverita.brefdic.datasource

object DataSourceResolver {
  
  val bound = 90000
  
  def resolve(wordId : Int) = if (wordId <= bound) 1 else 2
  
  def calculateWordId(page : Int, size : Int) = page * size
  
  def resolvePage(page : Int, size : Int) = {
    val wordId = calculateWordId(page, size)
    val dataSource = resolve(wordId)
    if (dataSource == 1) page 
    else (wordId - bound) / size
  }

}