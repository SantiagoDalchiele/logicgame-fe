package uy.com.uma.logicgame.fe.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.comun.util.UtilFormato;
import uy.com.uma.comun.util.UtilIO;
import uy.com.uma.logicgame.api.persistencia.ICargadorRecursos;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;

/**
 * Implementa la lógica web de administración de persistencia del sistema
 * @see uy.com.uma.logicgame.adm.CargadorRecursos;
 */
@WebServlet(name="AdmUploadServlet", urlPatterns = {"/jsp/upload"})
@MultipartConfig
public class AdmUploadServlet extends DBAccessServlet implements ILogicGameWebConstants {

	private static final long serialVersionUID = -8598707417881818101L;
	
	private static final Logger log = LogManager.getLogger(AdmUploadServlet.class.getName());

	
	/** Constantes para los parametros de la página */
	public static final String ATT_CLAVE = "clave";
	private static final String PARAM_FILE = "file";
	private static final String PARAM_IDIOMA = "idioma";
	
	

	/**
	 * Setea el archivo de configuración del sistema logic-game
	 */
	@Override
	public void init() throws ServletException {
		initConfiguracion();		
		setUsuario(configuracion.getAdmDBUser());
		setClave(configuracion.getAdmDBPassword());
		super.init();
	}



	/**
	 * Redirecciona el request
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	
	
	/**
	 * Redirecciona el request
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}
	

	
	/**
	 * Obtiene del multi-part request el archivo y utiliza la clase CargadorRecursos para procesar los archivos
	 * y realizar la persistencia en el sistema
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html;charset=UTF-8");
	    final Part filePart = request.getPart(PARAM_FILE);
	    final String extArch = UtilIO.extFile(getFileName(filePart));
	    final PrintWriter writer = response.getWriter();
	    final String idioma = request.getParameter(PARAM_IDIOMA);
	    boolean dbIniciada = false;
	    log.debug("Idioma=" + idioma + ", Extension=" + extArch);
	    

	    try {
	    	if (request.getSession().getAttribute(ID_ATT_CLAVE_ADM) == null)
	    		writer.println("Usuario no logeado para adminstrar");
	    	else {
	    		final String clave = request.getSession().getAttribute(ID_ATT_CLAVE_ADM).toString();
	    		
	    		if (!clave.equals(configuracion.getAdmPassword()))
	    			writer.println("Clave de administracion incorrecta");
	    		else {	    	
			    	if (extArch.equalsIgnoreCase("zip")) {
			    		dbIniciada = true;
			    		initDB();
				        ICargadorRecursos loader = PersistenciaFactory.getInstancia().getCargadorRecursos();
				        loader.setInputStream(filePart.getInputStream());
				        
				        if (!UtilFormato.esNulo(idioma))
				        	loader.setIdioma(idioma);
				        
				        loader.cargar();		        
				        String result = "<table>";
				        
				        for (String arch : loader.getArchivosProcesados())
				        	result += "<tr><td>" + arch + "</td></tr>";		        
				        
				        result += "</table>";
				        writer.println(result);
			    	} else
			    		writer.println("La extensión del archivo debe ser .zip");
			    	}
	    	}
	    } catch (Exception e) {
	        writer.println("Error general de la aplicación");
	        writer.println("<br/> ERROR: " + e.getMessage());
	        log.fatal("Problems during file upload. Error: {0}", e);
	    } finally {
	    	UtilIO.closeWriter(writer);
	    	
	    	if (dbIniciada)
	    		try {
	    			PersistenciaFactory.getInstancia().getManejadorSesiones().shutdown();
	    		} catch (Exception e) {
	    			log.warn("Error al cerrar sesion de base de datos", e);
	    		}
	    }
	}
	
	
	
	/**
	 * Retorna el nombre del archivo dada la "parte" del request (la parte que contiene el archivo)
	 */
	private static String getFileName (final Part part) {
	    final String partHeader = part.getHeader("content-disposition");	    
	    
	    for (String content : partHeader.split(";"))
	        if (content.trim().startsWith("filename"))
	            return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
	    
	    return null;
	}	
}
