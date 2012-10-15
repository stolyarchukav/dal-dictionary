package forzaverita.brefdic.datasource

object DataSourceHolder {

  private val contextHolder : ThreadLocal[Integer] = new ThreadLocal()
  
  def := (number : Integer) {
	contextHolder.set(number);
  }
 
  def getDataSourceNum() = contextHolder.get()
    
}