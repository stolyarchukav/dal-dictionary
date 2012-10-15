package forzaverita.brefdic

object DataImport {
  
  def main(args : Array[String]) : Unit = {
    println (Database.getInstance().getCount())
    println (Database.getInstance().getWord(1760).getDescription().length())
  }
  
}
