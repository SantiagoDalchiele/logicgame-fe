package uy.com.uma.logicgame.fe.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uy.com.uma.comun.util.UtilAJAX;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;
import uy.com.uma.logicgame.fe.web.adm.actions.AdmAbstractAction;
import uy.com.uma.logicgame.fe.web.adm.actions.UploadAction;

/**
 * Implementa la lógica web de administración de persistencia del sistema
 */
@WebServlet(name="AdmUploadServlet", urlPatterns = {"/jsp/upload"})
@MultipartConfig
public class AdmUploadServlet extends DBAccessServlet implements ILogicGameWebConstants {

	private static final long serialVersionUID = -8598707417881818101L;
	
	private AdmAbstractAction uploadAction = new UploadAction();
	
	

	/**
	 * Setea el archivo de configuración del sistema logic-game
	 */
	@Override
	public void init() throws ServletException {
		initConfiguracion();		
		uploadAction.setConfiguracion(configuracion);
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
		UtilAJAX.initAjaxResponse(response);
		uploadAction.perform(request, response.getWriter());
	}
}
