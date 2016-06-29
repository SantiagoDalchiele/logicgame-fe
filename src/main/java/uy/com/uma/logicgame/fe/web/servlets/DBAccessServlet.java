package uy.com.uma.logicgame.fe.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.logicgame.api.conf.IConfiguracionConstantes;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.Configuracion;

/**
 * Servlet que encapsula el acceso a base de datos
 *
 * @author Santiago Dalchiele
 */
abstract class DBAccessServlet extends HttpServlet implements IConfiguracionConstantes {

	private static final long serialVersionUID = -1627197938998512487L;
	
	private static final Logger log = LogManager.getLogger(DBAccessServlet.class.getName());
	
	
	/** Configuración del sistema */
	protected Configuracion configuracion;
	
	/** Usuario y clave de acceso a la base de datos */
	protected String usuario;
	protected String clave;
	
	
	
	/**
	 * Metodos de acceso	
	 */
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}

	
	
	/**
	 * Inicializa el acceso a la base de datos
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		initConfiguracion();
		initDB();
	}

	
	
	/**
	 * Destruye "basura" de la conexion a hibernate
	 */
	@Override
	public void destroy() {
		super.destroy();
		
		try {
			PersistenciaFactory.getInstancia().getManejadorSesiones().shutdown();
		} catch (Exception e) {
			log.warn("Error al cerrar sesion de base de datos", e);
		}
	}

	
	
	/**
	 * Inicializa la configuracion del sistema (logicgame.properties)
	 */
	protected void initConfiguracion() throws ServletException {
		try {
			configuracion = Configuracion.getInstancia();
		} catch (Exception e) {
			log.fatal("Error al obtener la configuración del sistema", e);
			throw new ServletException("Error al obtener la configuración del sistema", e);
		}
	}


	
	/**
	 * Inicializa el acceso a la base de datos
	 */
	protected void initDB() throws ServletException {
		try {
			PersistenciaFactory.getInstancia().getManejadorSesiones().reset(usuario, clave);
		} catch (Exception e) {
			throw new ServletException("Error al configurar la conexion a la base de datos", e);
		}
	}
}
