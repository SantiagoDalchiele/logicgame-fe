package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uy.com.uma.logicgame.fe.web.ActionsHelper;
import uy.com.uma.logicgame.fe.web.Configuracion;
import uy.com.uma.logicgame.fe.web.ILogicGameWebConstants;


/**
 * Define la clase base para las acciones que procesa la fachada de administracion del juego
 * @see uy.com.uma.logicgame.fe.web.servlets.FachadaAdmLgServlet
 *
 * @author Santiago Dalchiele
 */
public abstract class AdmAbstractAction extends ActionsHelper implements ILogicGameWebConstants {

	/** Constantes que definen los requerimientos AJAX */
	protected static final String ID_REQ_LOGIN = "login.admdo";
	protected static final String ID_REQ_CREAR_TABLAS = "crear_tablas.admdo";
	protected static final String ID_REQ_BORRAR_TABLAS = "borrar_tablas.admdo";
	protected static final String ID_REQ_BORRAR_DATOS = "borrar_datos.admdo";
	protected static final String ID_REQ_CREAR_RUTA_X_DEFECTO = "crear_ruta_x_defecto.admdo";
	protected static final String ID_REQ_GET_IDIOMAS = "getIdiomas.admdo";
	protected static final String ID_REQ_GET_JUEGOS = "getJuegos.admdo";
	protected static final String ID_REQ_GET_RUTAS = "getRutas.admdo";
	protected static final String ID_REQ_GET_USUARIOS = "getUsuarios.admdo";
	protected static final String ID_REQ_PARCHE_TOKEN = "parche01.admdo";
	
	
	/** Configuración del sistema */
	protected Configuracion configuracion;
	
	
	
	/** Retorna el nombre de la acción */
	public abstract String getName();
	
	/** Ejecuta la acción */
	public abstract void perform (HttpServletRequest req, PrintWriter out) throws ServletException, IOException;
	
	
	
	/**
	 * Retorna TRUE si hace uso de la conexión a base de datos
	 */
	public boolean usaConexionBD() {
		return false;
	}
	
	
	
	/**
	 * Setea la configuración del sistema
	 */
	public void setConfiguracion (Configuracion conf) {
		this.configuracion = conf;
	}
	
	
	
	/**
	 * Chequea que esté logeado y que la clave sea correcta, en casa contrario ya escribe el error en el OutWriter
	 */
	protected boolean checkLogin (HttpServletRequest req, PrintWriter out) {
		if (req.getSession().getAttribute(ID_ATT_CLAVE_ADM) == null)
			out.write(getErrorJSON ("Usuario no logeado para adminstrar"));
		else {
			final String clave = req.getSession().getAttribute(ID_ATT_CLAVE_ADM).toString();				
		
			if (!clave.equals(configuracion.getAdmPassword()))
				out.write(getErrorJSON ("Clave de administracion incorrecta"));
			else
				return true;
		}
		
		return false;
	}
}
