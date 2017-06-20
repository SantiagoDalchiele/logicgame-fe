package uy.com.uma.logicgame.fe.web;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * Lanza tomcat como un servicio y agrega esta aplicación
 *
 * @author Santiago Dalchiele
 */
public class LaunchTomcat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String webappDirLocation = "src/main/webapp/";
	        Tomcat tomcat = new Tomcat();
	        String webPort = System.getenv("PORT");
	        
	        if(webPort == null || webPort.isEmpty()) {
	            webPort = "8080";
	        }
	
	        tomcat.setPort(Integer.parseInt(webPort));
	        String contextPath = "/";
	        StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath, new File(webappDirLocation).getAbsolutePath());
	        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
	
	        // Declare an alternative location for your "WEB-INF/classes" dir
	        // Servlet 3.0 annotation will work
	        File additionWebInfClasses = new File("target/classes");
	        WebResourceRoot resources = new StandardRoot(ctx);
	        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
	        ctx.setResources(resources);
	        //tomcat.addServlet(contextPath, "CrearRoles", new CrearRolesServlet());
	        tomcat.start();
	        tomcat.getServer().await();
		} catch (ServletException | LifecycleException e) {
			System.out.println("No fue posible levantar Tomcat");
			e.printStackTrace();
		}
	}
}
