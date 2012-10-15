package forzaverita.brefdic.datasource

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import Predef.{ int2Integer => _, _ } 

class RoutingDataSource extends AbstractRoutingDataSource {

  override def determineCurrentLookupKey() = DataSourceHolder.getDataSourceNum
  
}