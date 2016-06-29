package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.logicgame.fe.web.Configuracion;


/**
 * Define la clase base para las acciones que procesa la fachada de administracion del juego
 * @see uy.com.uma.logicgame.fe.web.servlets.FachadaAdmLgServlet
 *
 * @author Santiago Dalchiele
 */
public abstract class AdmAbstractAction {

	/** Constantes que definen los requerimientos AJAX */
	protected static final String ID_REQ_LOGIN = "login.admdo";
	protected static final String ID_REQ_CREAR_ROLES = "crear_roles.admdo";
	protected static final String ID_REQ_CREAR_TABLAS = "crear_tablas.admdo";
	protected static final String ID_REQ_ASIGNAR_PERMISOS = "asignar_permisos.admdo";
	protected static final String ID_REQ_CREAR_RUTA_X_DEFECTO = "crear_ruta_x_defecto.admdo";
	
	
	/** Configuración del sistema */
	protected Configuracion configuracion;
	
	
	
	/** Retorna el nombre de la acción */
	public abstract String getName();
	
	/** Ejecuta la acción */
	public abstract void perform (HttpServletRequest req, PrintWriter out) throws ServletException, IOException;
	
	
	
	/**
	 * Setea la configuración del sistema
	 */
	public void setConfiguracion (Configuracion conf) {
		this.configuracion = conf;
	}
}
