package uy.com.uma.logicgame.fe.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.comun.util.UtilAJAX;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.Configuracion;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;
import uy.com.uma.logicgame.fe.web.actions.JuegoAbstractAction;
import uy.com.uma.logicgame.fe.web.adm.actions.AdmAbstractAction;
import uy.com.uma.logicgame.fe.web.adm.actions.BorrarDatosAction;
import uy.com.uma.logicgame.fe.web.adm.actions.BorrarTablasAction;
import uy.com.uma.logicgame.fe.web.adm.actions.CrearRutaXDefectoAction;
import uy.com.uma.logicgame.fe.web.adm.actions.CrearTablasAction;
import uy.com.uma.logicgame.fe.web.adm.actions.GetIdiomasAction;
import uy.com.uma.logicgame.fe.web.adm.actions.GetJuegosAction;
import uy.com.uma.logicgame.fe.web.adm.actions.GetRutasAction;
import uy.com.uma.logicgame.fe.web.adm.actions.GetUsuariosAction;
import uy.com.uma.logicgame.fe.web.adm.actions.LoginAction;
import uy.com.uma.logicgame.fe.web.adm.actions.Parche01Action;
import uy.com.uma.logicgame.fe.web.adm.actions.Parche02Action;

/**
 * Atiende los eventos producidos por la interface web de administracion (sus requerimientos AJAX)
 *
 * @author Santiago Dalchiele
 */
@WebServlet(name="FachadaAdmLg", urlPatterns = {"*.admdo"})
public class FachadaAdmLgServlet extends HttpServlet implements ILogicGameWebConstants {

	private static final long serialVersionUID = 6030892697129241355L;
	
	private static final Logger log = LogManager.getLogger(FachadaAdmLgServlet.class.getName());
	
	
	/** Configuración del sistema */
	private Configuracion configuracion;
	
	/** Mapeo de acciones */
	private Map<String, AdmAbstractAction> acciones = new HashMap<String, AdmAbstractAction>();
	
	/** Indica si esta inicializada la conexión a la base de datos */
	private boolean bdInicializada = false;
	
	
	
	/**
	 * Inicializa la configuracion del sistema (logicgame.properties y logicgame-conf.xml)
	 */
	@Override
	public void init() throws ServletException {
		super.init();		
		
		try {
			configuracion = Configuracion.getInstancia();
			List<AdmAbstractAction> accs = new ArrayList<AdmAbstractAction>();
			accs.add(new BorrarDatosAction());
			accs.add(new BorrarTablasAction());
			accs.add(new CrearRutaXDefectoAction());
			accs.add(new CrearTablasAction());
			accs.add(new GetIdiomasAction());
			accs.add(new GetJuegosAction());
			accs.add(new GetRutasAction());
			accs.add(new GetUsuariosAction());
			accs.add(new LoginAction());			
			accs.add(new Parche01Action());
			accs.add(new Parche02Action());
			
			for (AdmAbstractAction acc : accs) {
				acc.setConfiguracion(configuracion);
				acciones.put(acc.getName(), acc);
			}
		} catch (KeyException | IOException e) {
			log.fatal("Error al obtener la configuración del sistema", e);
			throw new ServletException("Error al obtener la configuración del sistema", e);
		}
	}
	
	
	
	/**
	 * Inicializa el acceso a la base de datos
	 */
	protected synchronized void initDB() throws ServletException {
		try {
			PersistenciaFactory.getInstancia().getManejadorSesiones().reset();
			bdInicializada = true;
		} catch (Exception e) {
			throw new ServletException("Error al configurar la conexion a la base de datos", e);
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
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		try {
			final String reqUri = req.getRequestURI();
			final String peticion = reqUri.substring(reqUri.lastIndexOf("/")+1);
			PrintWriter out = response.getWriter();
			
			if (!acciones.containsKey(peticion))
				out.write(JuegoAbstractAction.getErrorJSON("No se puede procesar la peticion [" + peticion + "]"));
			else {
				UtilAJAX.initAjaxResponse(response);
				log.debug("Procesando el request: " + reqUri + ", la peticion es del metodo: " + peticion);
				AdmAbstractAction accion = acciones.get(peticion);
				
				synchronized (this) {
					if (accion.usaConexionBD() && (!bdInicializada))
						initDB();
				}
				
				accion.perform(req, response.getWriter());
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.fatal("Error al procesar la solicitud de administracion logicgame", e);
			response.getWriter().write(JuegoAbstractAction.getErrorJSON(e));		
		}
	}	
}