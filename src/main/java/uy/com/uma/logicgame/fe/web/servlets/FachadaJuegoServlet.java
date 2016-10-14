package uy.com.uma.logicgame.fe.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.comun.util.UtilAJAX;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.BackgroundThread;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;
import uy.com.uma.logicgame.fe.web.actions.EnviarTokenAction;
import uy.com.uma.logicgame.fe.web.actions.EstaLogeadoAction;
import uy.com.uma.logicgame.fe.web.actions.FinJuegoAction;
import uy.com.uma.logicgame.fe.web.actions.GetConfiguracionAction;
import uy.com.uma.logicgame.fe.web.actions.GetJuegoAction;
import uy.com.uma.logicgame.fe.web.actions.GetRankingAction;
import uy.com.uma.logicgame.fe.web.actions.GrabarJuegoAction;
import uy.com.uma.logicgame.fe.web.actions.IResponseAction;
import uy.com.uma.logicgame.fe.web.actions.JuegoAbstractAction;
import uy.com.uma.logicgame.fe.web.actions.LoginAction;
import uy.com.uma.logicgame.fe.web.actions.LogoutAction;
import uy.com.uma.logicgame.fe.web.actions.RecibirTokenAction;
import uy.com.uma.logicgame.fe.web.actions.RegistroAction;
import uy.com.uma.logicgame.fe.web.actions.ReiniciarJuegoAction;
import uy.com.uma.logicgame.fe.web.actions.ResetClaveAction;
import uy.com.uma.logicgame.fe.web.actions.SetConfiguracionAction;
import uy.com.uma.logicgame.fe.web.actions.SetValorAction;

/**
 * Atiende los eventos producidos por el juego en la interface web y sus requerimientos AJAX
 *
 * @author Santiago Dalchiele
 */
@WebServlet(name="FachadaJuego", urlPatterns = {"*.do"})
public class FachadaJuegoServlet extends DBAccessServlet implements ILogicGameWebConstants {	         

	private static final long serialVersionUID = 7118299650020182029L;
	
	private static final Logger log = LogManager.getLogger(FachadaJuegoServlet.class.getName());
	
	
	/** Mapeo de acciones */
	private Map<String, JuegoAbstractAction> acciones = new HashMap<String, JuegoAbstractAction>();
	

	
	/**
	 * Inicializa el acceso a la base de datos
	 */
	@Override
	public void init() throws ServletException {
		initConfiguracion();
		super.init();		
		String path = super.getServletContext().getRealPath("");
		List<JuegoAbstractAction> accs = new ArrayList<JuegoAbstractAction>();
		
		try {
			accs.add(new EnviarTokenAction());
			accs.add(new EstaLogeadoAction());
			accs.add(new FinJuegoAction());
			accs.add(new GetConfiguracionAction());
			accs.add(new GetJuegoAction());
			accs.add(new GetRankingAction());
			accs.add(new GrabarJuegoAction());
			accs.add(new LoginAction());
			accs.add(new LogoutAction());
			accs.add(new RecibirTokenAction());
			accs.add(new RegistroAction());
			accs.add(new ReiniciarJuegoAction());
			accs.add(new ResetClaveAction());
			accs.add(new SetConfiguracionAction());
			accs.add(new SetValorAction());
		} catch (LogicGameException e) {
			throw new ServletException("Error al crear las acciones", e);
		}		
		
		for (JuegoAbstractAction accion : accs) {
			accion.setConfiguracion(configuracion);
			accion.setAppRealPath(path);
			acciones.put(accion.getName(), accion);
		}		
	}

	
	
	/**
	 * Redirecciona al get
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	

	
	
	/**
	 * Atiende los distintos requerimientos AJAX
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		try {
			final String reqUri = request.getRequestURI();
			final String peticion = reqUri.substring(reqUri.lastIndexOf("/")+1);
			PrintWriter out = response.getWriter();
			
			if (!acciones.containsKey(peticion))
				out.write(JuegoAbstractAction.getErrorJSON("No se puede procesar la peticion [" + peticion + "]"));
			else {					
				UtilAJAX.initAjaxResponse(response);
				checkDB(request.getSession());
				log.debug("Procesando el request: " + reqUri + ", la peticion es del metodo: " + peticion);				
				JuegoAbstractAction accion = acciones.get(peticion); 
				
				if (accion.sessionOK(request, out)) {
					if (accion instanceof IResponseAction)
						((IResponseAction) accion).setServletResponse(response);
					
					accion.perform(request, out);
				}
			}
		} catch (Exception e) {			
			log.fatal("Error al procesar la solicitud del juego", e);
			response.getWriter().write(JuegoAbstractAction.getErrorJSON(e));
		}
	}
	
	

	/**
	 * Chequea que siga viva la conexión, sino inicializa todo de vuelta con el usuario web de acceso a la base de datos
	 */
	private void checkDB (HttpSession session) throws ServletException {
		try {
			if (!PersistenciaFactory.getInstancia().getManejadorSesiones().isInitialized()) {
				initDB();

				for (JuegoAbstractAction accion : acciones.values())
					accion.reset();
				
				if (session.getAttribute(ID_ATT_THREAD_BACKGROUND) != null) {
					BackgroundThread hiloBackground = (BackgroundThread) session.getAttribute(ID_ATT_THREAD_BACKGROUND);
					hiloBackground.reset();
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException
				| LogicGameException e) {
			throw new ServletException("Error al chequear la conexion a base de datos", e);
		}
	}
}
