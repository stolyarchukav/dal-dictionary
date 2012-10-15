package forzaverita.brefdic.servlet

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.web.context.support.WebApplicationContextUtils
import forzaverita.brefdic.repo.WordRepository
import java.io.File
import java.net.URL

class OpenServlet extends HttpServlet {

  override def doGet(req : HttpServletRequest, resp : HttpServletResponse) {
    val path = req.getPathInfo()
    val id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1))
    val ctx = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext())
    val repo = ctx.getBean(classOf[WordRepository])
    val word = repo.findOne(id)
    val writer = resp.getWriter()
    writer.println("""<head>
    		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    		<title>Установите приложение \"Энциклопедия Брокгауза и Ефрона\"</title>
    		</head>""")
    writer.println("<br> File = " + new File(".").getAbsolutePath())
    //new URL("http://google.com").getContent();
    if (word != null) {
    	writer.println("<p>Слово: " + word.word)
    	writer.println("<p>Описание: " + word.description)
    }
    writer.println("""<p><a href='https://play.google.com/store/apps/details?id=org.forzaverita.brefdic'>Установите приложение \"Энциклопедия Брокгауза и Ефрона\"</a>
    </body></html>""")
    writer.close()
  }
  
}