package forzaverita.brefdic.rest.security

import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import java.io.Serializable

class RestPermissionEvaluator extends PermissionEvaluator {

  def hasPermission(authentication : Authentication, user : Any, permission : Any) = {
    if (permission == "read_app") {
    	user == "app_user"
    }
    else if (permission == "write_app") {
      user == "app_editor"
    }
    else false
  }
  
  def hasPermission(authentication : Authentication, targetId : Serializable, targetType : String, permission : Any) = {
    println(targetType)
    false
  }
  
}